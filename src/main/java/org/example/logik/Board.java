package org.example.logik;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board {
    // 1 = Bauer
    // 2 = Turm
    // 3 = Springer
    // 4 = Leufer
    // 5 = Dame
    // 6 = Koenig
    // Die werte in negativ sind schwarz
    boolean isWhite = true;

    Board() {
        for (int i = 0; i < board.length; i++) {
            if (i < 23) board[i] = 9;
            if (i > 120) board[i] = 9;
            if (i % 12 > 9) board[i] = 9;
            if (i % 12 < 2) board[i] = 9;
        }
    }

    int[] board = new int[144];

    ArrayList<Move> history = new ArrayList<>();
    public Board setFEN(String FEN) {
        String[] rows = FEN.split("/");
        int counter;
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

    boolean isControlled(int field, boolean byBlack) {
        return isControlled(field, byBlack, board);
    }

    boolean isControlled(int field, boolean byBlack, int[] board) {
        Stream<Move> all = Stream.of(new Move[]{new Move(1, 2, 3)});
        for (int i = 25; i < 118; i++) {
            if (board[i] == 0 || (byBlack) ? board[i] > 0 : board[i] < 0 || getMovesWithoutCheck(i, board) == null)
                continue;
            all = Stream.of(all, getMovesWithoutCheck(i, board)).flatMap(Function.identity());
        }
        return all.filter(Objects::nonNull).anyMatch(e -> e.to() == field);
    }

    Stream<Move> getMoves(int field) {
        Stream.Builder<Move> sb = Stream.builder();
        if (isControlled(getKing(board[field] < 0), board[field] > 0)) {
            int[] tempBoard = new int[board.length];
            getMovesWithoutCheck(field).forEach(e -> {
                System.arraycopy(board, 0, tempBoard, 0, board.length);
                move(e, tempBoard);
                if (!isControlled(getKing(board[field] < 0), board[field] > 0, tempBoard)) sb.add(e);
            });
            return Stream.concat(sb.build(), kingMoves(getKing(board[field] < 0), board[field] > 0));
        }
        return null;
    }

    Stream<Move> getMovesWithoutCheck(int field) {
        return getMovesWithoutCheck(field, board);
    }

    Stream<Move> getMovesWithoutCheck(int field, int[] board) {

        boolean isBlack = board[field] < 0;
        switch (Math.abs(board[field])) {
            case 1 -> {
                return this.pawnMoves(field, isBlack,board);
            }
            case 2 -> {
                return this.rookMoves(field, isBlack,board);
            }
            case 3 -> {
                return this.ponyMoves(field, isBlack,board);
            }
            case 4 -> {
                return this.bishopMoves(field, isBlack,board);
            }
            case 5 -> {
                return this.queenMoves(field, isBlack,board);
            }
            case 6 -> {
                return this.kingMoves(field, isBlack,board);
            }
            default -> {
                return Stream.empty();
            }
        }
    }

    Stream<Move> kingMoves(int i, boolean isBlack, int[] board) {
        return Stream.empty();
    }
    Stream<Move> kingMoves(int i, boolean isBlack) {
        return kingMoves(i,isBlack,board);
    }

    Stream<Move> pawnMoves(int i, boolean isBlack, int[] board) {
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

    Stream<Move> pawnMoves(int i, boolean isBlack) {
        return pawnMoves(i, isBlack, board);
    }

    Stream<Move> bishopMoves(int i, boolean isBlack) {
        return bishopMoves(i, isBlack, board);
    }

    Stream<Move> bishopMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(Stream.concat(Stream.concat(getLineFields(i, 11, isBlack, board[i],board), getLineFields(i, 13, isBlack, board[i],board)), getLineFields(i, -13, isBlack, board[i],board)), getLineFields(i, -11, isBlack, board[i],board));
    }

    Stream<Move> rookMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(Stream.concat(Stream.concat(getLineFields(i, 1, isBlack, board[i],board), getLineFields(i, -1, isBlack, board[i],board)), getLineFields(i, -12, isBlack, board[i],board)), getLineFields(i, 12, isBlack, board[i],board));
    }
    Stream<Move> rookMoves(int i, boolean isBlack) {
        return rookMoves(i,isBlack,board);
    }

    Stream<Move> queenMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(bishopMoves(i, isBlack,board), rookMoves(i, isBlack,board));
    }
    Stream<Move> queenMoves(int i, boolean isBlack) {
        return queenMoves(i,isBlack,board);
    }

    Stream<Move> getLineFields(int start, int position, boolean isBlack, int pice, int[] board) {
        Stream.Builder<Move> sb = Stream.builder();
        int j = start;
        while ((board[j + position] == 0 || ((isBlack) ? board[j + position] > 0 : board[j + position] < 0)) && board[j + position] != 9) {
            j += position;
            sb.add(new Move(start, j, pice));
            if ((isBlack) ? board[j] == 6 : board[j] == -6) continue;
            if (((isBlack) ? board[j] > 0 : board[j] < 0)) break;
        }
        return sb.build();
    }
    Stream<Move> getLineFields(int start, int position, boolean isBlack, int pice) {
        return getLineFields(start,position,isBlack,pice,board);
    }

    int getKing(boolean isBlack) {
        for (int i = 0; i < board.length; i++) {
            if ((isBlack) ? board[i] == -6 : board[i] == 6) return i;
        }
        return 0;
    }

    Stream<Move> ponyMoves(int i, boolean isBlack) {
        return ponyMoves(i,isBlack,board);
    }
    Stream<Move> ponyMoves(int i, boolean isBlack,int[] board) {
        Stream.Builder<Move> sb = Stream.builder();
        sb.add(getPonyMove(i, 1, 12, isBlack, board[i],board));
        sb.add(getPonyMove(i, 1, -12, isBlack, board[i],board));
        sb.add(getPonyMove(i, -1, 12, isBlack, board[i],board));
        sb.add(getPonyMove(i, -1, -12, isBlack, board[i],board));
        sb.add(getPonyMove(i, 12, 1, isBlack, board[i],board));
        sb.add(getPonyMove(i, 12, -1, isBlack, board[i],board));
        sb.add(getPonyMove(i, -12, 1, isBlack, board[i],board));
        sb.add(getPonyMove(i, -12, -1, isBlack, board[i],board));
        return sb.build().filter(Objects::nonNull);
    }

    Move getPonyMove(int start, int dir1, int dir2, boolean isBlack, int pice,int[] board) {
        return ((board[start + dir1 + dir1 + dir2] == 0 || ((isBlack) ? board[start + dir1 + dir1 + dir2] > 0 : board[start + dir1 + dir1 + dir2] < 0)) && board[start + dir1 + dir1 + dir2] != 9) ? new Move(start, start + dir1 + dir1 + dir2, pice) : null;
    }
    Move getPonyMove(int start, int dir1, int dir2, boolean isBlack, int pice) {
        return getPonyMove(start, dir1, dir2, isBlack, pice, board);
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

    private boolean move(Move move, int[] board) {
        board[move.from()] = 0;
        board[move.to()] = move.pice();
        return true;
    }
    boolean move(Move move) {
        if(isWhite == move.pice() > 0 || getMoves(move.from()).noneMatch(e ->e.to() == move.to())) return false;
        board[move.from()] = 0;
        board[move.to()] = move.pice();
        return true;
    }
}
