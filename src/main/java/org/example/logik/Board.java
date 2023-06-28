package org.example.logik;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Board {
    Board() {
        for (int i = 0; i < board.length; i++) {
            if (i < 24) board[i] = 9;
            if (i > 120) board[i] = 9;
            if (i % 12 > 9) board[i] = 9;
            if (i % 12 < 2) board[i] = 9;
        }
    }

    public char getPice(int i) {
        switch (Math.abs(board[i])) {
            case 1 -> {
                return (board[i] > 0) ? 'P' : 'p';
            }
            case 2 -> {
                return (board[i] > 0) ? 'R' : 'r';
            }
            case 3 -> {
                return (board[i] > 0) ? 'N' : 'n';
            }
            case 4 -> {
                return (board[i] > 0) ? 'B' : 'b';
            }
            case 5 -> {
                return (board[i] > 0) ? 'D' : 'd';
            }
            case 6 -> {
                return (board[i] > 0) ? 'K' : 'k';
            }
        }
        return 'X';
    }

    public Board setFEN(String FEN) {
        String[] rows = FEN.split("/");
        int counter = 0;
        int pice = 0;
        for (int i = 0; i < rows.length; i++) {
            char[] row = rows[i].toCharArray();
            counter = 0;
            for (char c : row) {
                if (Character.isDigit(c)) counter += Character.getNumericValue(c);
                else {
                    switch (Character.toLowerCase(c)) {
                        case 'p' -> pice = (Character.isUpperCase(c)) ? 1 : -1;
                        case 'r' -> pice = (Character.isUpperCase(c)) ? 2 : -2;
                        case 'n' -> pice = (Character.isUpperCase(c)) ? 3 : -3;
                        case 'b' -> pice = (Character.isUpperCase(c)) ? 4 : -4;
                        case 'q' -> pice = (Character.isUpperCase(c)) ? 5 : -5;
                        case 'k' -> pice = (Character.isUpperCase(c)) ? 6 : -6;
                    }
                    board[26 + (12 * i) + counter] = pice;
                    counter++;
                }
            }
        }
        return this;
    }

    int[] board = new int[144];
    boolean isWhite = true;
    ArrayList<Move> history = new ArrayList<>();

    // 1 = Bauer
    // 2 = Turm
    // 3 = Springer
    // 4 = Leufer
    // 5 = Dame
    // 6 = Koenig
    // Die werte in negativ sind schwarz

    Stream<Move> pawnMoves(int i, boolean isBlack) {
        Stream.Builder<Move> sb = Stream.builder();
        if (isBlack) {
            if (board[i + 12] == 0) {
                if (110 <= i && 118 >= i) sb.add(new Move(i, i + 12, -5));
                else if (board[i + 24] == 0 && 38 <= i && 46 >= i) sb.add(new Move(i, i + 24, -1));
                else sb.add(new Move(i, i + 12, -1));

            }
        } else {
            if (board[i - 12] == 0) {
                if (38 <= i && 46 >= i) sb.add(new Move(i, i - 12, +5));
                else sb.add(new Move(i, i - 12, 1));
                if (board[i - 24] == 0 && 97 <= i && 105 >= i) sb.add(new Move(i, i - 24, 1));
            }
        }
        return sb.build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            sb.append(board[i]);
            if ((i + 1) % 12 == 0) sb.append('\n');
        }
        return sb.toString();
    }

    boolean move(Move move) {
        return false;
    }
}
