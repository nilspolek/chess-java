package com.github.nilspolek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import processing.data.JSONObject;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board extends Thread implements Chessable {
    // 1 = Bauer
    // 2 = Turm
    // 3 = Springer
    // 4 = Leufer
    // 5 = Dame
    // 6 = Koenig
    // Die werte in negativ sind schwarz
    Move bestMove = null;
    List<Move> bestMoves = new ArrayList<>();
    public boolean isWhite = true;
    int depth = 3;
    long TIME_LIMIT = 30000;
    Move lastBestMove;
    boolean[] movedPices = new boolean[]{false, false, false, false, false, false};
    public int[] board = new int[144];
    List<SaveingPoint> history = new LinkedList<>();

    public Board() {
        for (int i = 0; i < board.length; i++) {
            if (i < 23) board[i] = 9;
            if (i > 120) board[i] = 9;
            if (i % 12 > 9) board[i] = 9;
            if (i % 12 < 2) board[i] = 9;
        }
    }

    int[] getBoard(int[] board) {
        ArrayList<Integer> ib = new ArrayList<>();
        for (int i : board) if (i != 9) ib.add(i);
        return ib.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getBoard() {
        return getBoard(board);
    }

    public void saveGame(String path) {
        StringBuilder sb = new StringBuilder();
        for (SaveingPoint s : history) sb.append(getFEN(s)).append('\n');
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter myWriter = new FileWriter(path);
                myWriter.write(sb.toString());
                myWriter.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void loadGame(String path) {
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.setFEN(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
        history.add(new SaveingPoint(board, isWhite, movedPices));
    }

    public int evaluate() {
        if (isCheckMate() == 1) return 1000;
        if (isCheckMate() == -1) return -1000;
        int counter = 0;
        for (int i : board) {
            if (i == 9) continue;
            counter += getValue(i);
        }
        return counter;
    }
    public void run(){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TIME_LIMIT;
        lastBestMove = findBestMove(depth,!isWhite,endTime);
    }
    public void findBestMove(int depth,long TIME_LIMIT){
        this.depth = depth;
        this.TIME_LIMIT = TIME_LIMIT;
        this.start();
    }

    public void findBestMove() {
        depth = 3;
        this.start();
        TIME_LIMIT=30_000;
    }

    public Move findBestMove(int depth, boolean isBlack,long endTime) {
        boolean maximizingPlayer = true;
        int bestValue = Integer.MIN_VALUE;
        for (Move move : getAllMoves().toArray(Move[]::new)) {
            move(move);
            int value = minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !maximizingPlayer, isBlack,endTime);
            undoMove();
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (value == bestValue) {
                bestMoves.add(move);
            }
        }
        if (!bestMoves.isEmpty()) {
            int randomIndex = new Random().nextInt(bestMoves.size());
            bestMove = bestMoves.get(randomIndex);
        }
        return bestMove;
    }


    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer, boolean isBlack,long endTime) {
        if (depth == 0 || isCheckMate() != 0) return (isBlack) ? -evaluate() : evaluate();
        if (System.currentTimeMillis() >= endTime) return (isBlack) ? -evaluate() : evaluate();

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : getAllMoves().toArray(Move[]::new)) {
                move(move);
                int eval = minimax(depth - 1, alpha, beta, false, isBlack,endTime);
                undoMove();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : getAllMoves().toArray(Move[]::new)) {
                move(move);
                int eval = minimax(depth - 1, alpha, beta, true, isBlack,endTime);
                undoMove();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;

            }

            return minEval;
        }
    }

    public void undoMove() {
        if (history.size() == 1){
            return;
        }
        history.remove(history.size() - 1);
        board = Arrays.copyOf(history.get(history.size() - 1).board(), board.length);
        isWhite = history.get(history.size() - 1).isWhite();
        movedPices = Arrays.copyOf(history.get(history.size() - 1).movedPices(), movedPices.length);
    }

    public int getValue(int pice) {
        switch (Math.abs(pice)) {
            case 1 -> {
                return (pice > 0) ? 1 : -1;
            }
            case 2 -> {
                return (pice > 0) ? 5 : -5;
            }
            case 3, 4 -> {
                return (pice > 0) ? 3 : -3;
            }
            case 5 -> {
                return (pice > 0) ? 9 : -9;
            }
            case 6 -> {
                return (pice > 0) ? 100 : -100;
            }
            default -> {
                return 0;
            }
        }
    }

    public String getFEN() {
        if(history.size() == 0) return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        return getFEN(history.get(history.size() - 1));
    }

    public String getFEN(SaveingPoint saveingPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        int[] tempBoard = getBoard(saveingPoint.board());
        for (int i = 0; i < tempBoard.length; i++) {
            if (tempBoard[i] != 0 && counter != 0) {
                stringBuilder.append(counter);
                stringBuilder.append(getPiceFromPice(tempBoard[i]));
                counter = 0;
            } else if (0 == tempBoard[i]) {
                counter++;
            } else {
                stringBuilder.append(getPiceFromPice(tempBoard[i]));
            }
            if ((i + 1) % 8 == 0) {
                if (counter != 0) {
                    stringBuilder.append(counter);
                    counter = 0;
                }
                stringBuilder.append('/');
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append((saveingPoint.isWhite()) ? " w " : " b ");
        if (!saveingPoint.movedPices()[4]) {
            if (!saveingPoint.movedPices()[3]) stringBuilder.append('Q');
            if (!saveingPoint.movedPices()[5]) stringBuilder.append('K');
        }
        if (!saveingPoint.movedPices()[1]) {
            if (!saveingPoint.movedPices()[0]) stringBuilder.append('q');
            if (!saveingPoint.movedPices()[2]) stringBuilder.append('k');
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ' ') stringBuilder.append("-");
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ' ') stringBuilder.append("-");
        stringBuilder.append(" - ");
        stringBuilder.append(history.size() / 2);
        stringBuilder.append(" ").append(history.size());
        return stringBuilder.toString();
    }


    public Board setFEN(String FEN) {
        for (int i = 0; i < board.length; i++) if(board[i] != 9)board[i] = 0;
        if (FEN.split(" ").length > 2) {
            isWhite = FEN.split(" ")[1].equals('w');
            if (!FEN.split(" ")[2].equals('-')) {
                String movedpicesString = FEN.split(" ")[2];
                movedPices[0] = true;
                movedPices[1] = true;
                movedPices[2] = true;
                movedPices[3] = true;
                movedPices[4] = true;
                movedPices[5] = true;
                if (movedpicesString.indexOf('Q') != -1) {
                    movedPices[4] = false;
                    movedPices[3] = false;
                }
                if (movedpicesString.indexOf('K') != -1) {
                    movedPices[4] = false;
                    movedPices[5] = false;
                }
                if (movedpicesString.indexOf('q') != -1) {
                    movedPices[0] = false;
                    movedPices[1] = false;
                }
                if (movedpicesString.indexOf('k') != -1) {
                    movedPices[1] = false;
                    movedPices[2] = false;
                }
            }
        }
        FEN = FEN.split(" ")[0];
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
        history.add(new SaveingPoint(board, isWhite, movedPices));
        return this;
    }

    public char getPice(int i) {
        return getPiceFromPice(board[i]);
    }

    public char getPiceFromPice(int i) {
        switch (Math.abs(i)) {
            case 1 -> {
                return (i > 0) ? 'P' : 'p';
            }
            case 2 -> {
                return (i > 0) ? 'R' : 'r';
            }
            case 3 -> {
                return (i > 0) ? 'N' : 'n';
            }
            case 4 -> {
                return (i > 0) ? 'B' : 'b';
            }
            case 5 -> {
                return (i > 0) ? 'Q' : 'q';
            }
            case 6 -> {
                return (i > 0) ? 'K' : 'k';
            }
        }
        return 'X';
    }

    public boolean isControlled(int field, boolean byBlack) {
        return isControlled(field, byBlack, board);
    }

    boolean isControlled(int field, boolean byBlack, int[] board) {
        Stream<Move> all = Stream.of(new Move[]{new Move(1, 2, 3)});
        for (int i = 25; i < 118; i++) {
            if (board[i] == 0 || (byBlack) ? board[i] > 0 : board[i] < 0 || getMovesWithoutCheckExcludingKing(i, board) == null)
                continue;
            all = Stream.of(all, getMovesWithoutCheckExcludingKing(i, board)).flatMap(Function.identity());
        }
        return all.filter(Objects::nonNull).anyMatch(e -> e.to() == field);
    }

    public Stream<Move> getMoves(int field) {
        if (isWhite == (board[field] < 0)) return Stream.empty();
        Stream.Builder<Move> sb = Stream.builder();
        int[] tempBoard = new int[board.length];
        getMovesWithoutCheck(field).forEach(e -> {
            System.arraycopy(board, 0, tempBoard, 0, board.length);
            move(e, tempBoard);
            if (!isControlled(getKing(board[field] < 0), board[field] > 0, tempBoard)) sb.add(e);
        });
        return Stream.concat(sb.build(), kingMoves(getKing(board[field] < 0), board[field] < 0)).filter(e -> e.from() == field).distinct();
    }

    public int isCheckMate() {
        if (this.getAllMoves().noneMatch(e -> e.pice() < 0) && !isWhite) {
            if(!isControlled(getKing(true),false)) {
                System.out.println("Stalemate");
                return 2;
            }
            System.out.println("White won");
            return 1;
        }
        if (this.getAllMoves().noneMatch(e -> e.pice() > 0) && isWhite) {
            if(!isControlled(getKing(false),true)) {
                System.out.println("Stalemate");
                return -2;
            }
            System.out.println("Black won");
            return -1;
        }
        return 0;
    }

    Stream<Move> getMovesWithoutCheckExcludingKing(int field) {
        return getMovesWithoutCheckExcludingKing(field, board);
    }

    Stream<Move> getMovesWithoutCheck(int field) {
        return getMovesWithoutCheck(field, board);
    }

    Stream<Move> getMovesWithoutCheck(int field, int[] board) {

        boolean isBlack = board[field] < 0;
        switch (Math.abs(board[field])) {
            case 1 -> {
                return this.pawnMoves(field, isBlack, board);
            }
            case 2 -> {
                return this.rookMoves(field, isBlack, board);
            }
            case 3 -> {
                return this.ponyMoves(field, isBlack, board);
            }
            case 4 -> {
                return this.bishopMoves(field, isBlack, board);
            }
            case 5 -> {
                return this.queenMoves(field, isBlack, board);
            }
            case 6 -> {
                return this.kingMoves(field, isBlack, board);
            }
            default -> {
                return Stream.empty();
            }
        }
    }

    Stream<Move> getMovesWithoutCheckExcludingKing(int field, int[] board) {

        boolean isBlack = board[field] < 0;
        switch (Math.abs(board[field])) {
            case 1 -> {
                return this.pawnMoves(field, isBlack, board);
            }
            case 2 -> {
                return this.rookMoves(field, isBlack, board);
            }
            case 3 -> {
                return this.ponyMoves(field, isBlack, board);
            }
            case 4 -> {
                return this.bishopMoves(field, isBlack, board);
            }
            case 5 -> {
                return this.queenMoves(field, isBlack, board);
            }
            case 6 -> {
                Stream.Builder<Move> sb = Stream.builder();
                if (board[field - 11] == 0) sb.add(new Move(field, field - 11, (board[field] > 0) ? -6 : 6));
                if (board[field - 12] == 0) sb.add(new Move(field, field - 12, (board[field] > 0) ? -6 : 6));
                if (board[field - 13] == 0) sb.add(new Move(field, field - 13, (board[field] > 0) ? -6 : 6));
                if (board[field - 1] == 0) sb.add(new Move(field, field - 1, (board[field] > 0) ? -6 : 6));
                if (board[field + 1] == 0) sb.add(new Move(field, field + 1, (board[field] > 0) ? -6 : 6));
                if (board[field + 11] == 0) sb.add(new Move(field, field + 11, (board[field] > 0) ? -6 : 6));
                if (board[field + 12] == 0) sb.add(new Move(field, field + 12, (board[field] > 0) ? -6 : 6));
                if (board[field + 13] == 0) sb.add(new Move(field, field + 13, (board[field] > 0) ? -6 : 6));
                return sb.build();
            }
            default -> {
                return Stream.empty();
            }
        }
    }
    boolean isControlledAfterCapture(int i,boolean byBlack,int[] board){
        if(isControlled(i,byBlack,board) && ((byBlack)?i<1:i>-1)) return true;
        if(isControlled(i,byBlack,board)) return true;
        int[] temp = Arrays.copyOf(board,board.length);
        temp[i] = (byBlack)?1:-1;
        if(isControlled(i,byBlack,temp)) return true;
        return false;
    }

    Stream<Move> kingMoves(int i, boolean isBlack, int[] board) {
        Stream.Builder<Move> sb = Stream.builder();
        if (!isControlledAfterCapture(i - 11, !isBlack, board) && ((isBlack) ? board[i - 11] >= 0 : board[i - 11] <= 0) && board[i - 11] != 9)
            sb.add(new Move(i, i - 11, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i - 12, !isBlack, board) && ((isBlack) ? board[i - 12] >= 0 : board[i - 12] <= 0) && board[i - 12] != 9)
            sb.add(new Move(i, i - 12, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i - 13, !isBlack, board) && ((isBlack) ? board[i - 13] >= 0 : board[i - 13] <= 0) && board[i - 13] != 9)
            sb.add(new Move(i, i - 13, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i - 1, !isBlack, board) && ((isBlack) ? board[i - 1] >= 0 : board[i - 1] <= 0) && board[i - 1] != 9)
            sb.add(new Move(i, i - 1, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i + 1, !isBlack, board) && ((isBlack) ? board[i + 1] >= 0 : board[i + 1] <= 0) && board[i + 1] != 9)
            sb.add(new Move(i, i + 1, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i + 11, !isBlack, board) && ((isBlack) ? board[i + 11] >= 0 : board[i + 11] <= 0) && board[i + 11] != 9)
            sb.add(new Move(i, i + 11, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i + 12, !isBlack, board) && ((isBlack) ? board[i + 12] >= 0 : board[i + 12] <= 0) && board[i + 12] != 9)
            sb.add(new Move(i, i + 12, (isBlack) ? -6 : 6));
        if (!isControlledAfterCapture(i + 13, !isBlack, board) && ((isBlack) ? board[i + 13] >= 0 : board[i + 13] <= 0) && board[i + 13] != 9)
            sb.add(new Move(i, i + 13, (isBlack) ? -6 : 6));
        if (isBlack) {
            if (!movedPices[0] && !movedPices[1] && !isControlled(27, false, board) && !isControlled(28, false, board) && !isControlled(29, false, board) && !isControlled(30, false, board) && board[28] == 0 && board[29] == 0)
                sb.add(new Move(i, 28, -6));
            if (!movedPices[1] && !movedPices[2] && !isControlled(30, false, board) && !isControlled(31, false, board) && !isControlled(32, false, board) && board[31] == 0 && board[32] == 0)
                sb.add(new Move(i, 32, -6));
        } else {
            if (!movedPices[3] && !movedPices[4] && !isControlled(112, true, board) && !isControlled(113, true, board) && !isControlled(114, true, board) && board[111] == 0 && board[112] == 0 && board[113] == 0)
                sb.add(new Move(i, 112, 6));
            if (!movedPices[4] && !movedPices[5] && !isControlled(114, true, board) && !isControlled(115, true, board) && !isControlled(116, true, board) && board[115] == 0 && board[116] == 0)
                sb.add(new Move(i, 116, 6));
        }
        return sb.build();
    }

    public Stream<Move> kingMoves(int i, boolean isBlack) {
        return kingMoves(i, isBlack, board);
    }

    Stream<Move> pawnMoves(int i, boolean isBlack, int[] board) {
        Stream.Builder<Move> sb = Stream.builder();
        if (isBlack) {
            if (board[i + 11] > 0 && board[i+11] != 9) sb.add(new Move(i, i + 11, -1));
            if (board[i + 13] > 0  && board[i+13] != 9) sb.add(new Move(i, i + 13, -1));
            if (board[i + 12] == 0) {
                if (110 <= i && 118 >= i) sb.add(new Move(i, i + 12, -1));
                else sb.add(new Move(i, i + 12, -1));
                if (board[i + 24] == 0 && 38 <= i && 46 >= i) sb.add(new Move(i, i + 24, -1));
            }
        } else {
            if (board[i - 11] < 0  && board[i-11] != 9) sb.add(new Move(i, i - 11, 1));
            if (board[i - 13] < 0  && board[i-13] != 9) sb.add(new Move(i, i - 13, 1));
            if (board[i - 12] == 0) {
                if (38 <= i && 46 >= i) sb.add(new Move(i, i - 12, +1));
                else sb.add(new Move(i, i - 12, 1));
                if (board[i - 24] == 0 && 97 <= i && 105 >= i) sb.add(new Move(i, i - 24, 1));
            }
        }
        return sb.build();
    }

    public Stream<Move> getAllMoves() {
        Stream<Move> s = Stream.of(new Move(1, 1, 1));
        for (int i = 0; i < board.length; i++) {
            s = Stream.concat(getMoves(i), s);
        }
        return s.filter(e -> {
            return e.from() != 1 || e.to() != 1 || e.pice() != 1;
        }).distinct();
    }

    public Stream<Move> pawnMoves(int i, boolean isBlack) {
        return pawnMoves(i, isBlack, board);
    }

    public Stream<Move> bishopMoves(int i, boolean isBlack) {
        return bishopMoves(i, isBlack, board);
    }

    Stream<Move> bishopMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(Stream.concat(Stream.concat(getLineFields(i, 11, isBlack, board[i], board), getLineFields(i, 13, isBlack, board[i], board)), getLineFields(i, -13, isBlack, board[i], board)), getLineFields(i, -11, isBlack, board[i], board));
    }

    Stream<Move> rookMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(Stream.concat(Stream.concat(getLineFields(i, 1, isBlack, board[i], board), getLineFields(i, -1, isBlack, board[i], board)), getLineFields(i, -12, isBlack, board[i], board)), getLineFields(i, 12, isBlack, board[i], board));
    }

    public Stream<Move> rookMoves(int i, boolean isBlack) {
        return rookMoves(i, isBlack, board);
    }

    Stream<Move> queenMoves(int i, boolean isBlack, int[] board) {
        return Stream.concat(bishopMoves(i, isBlack, board), rookMoves(i, isBlack, board));
    }

    public Stream<Move> queenMoves(int i, boolean isBlack) {
        return queenMoves(i, isBlack, board);
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
        return getLineFields(start, position, isBlack, pice, board);
    }

    public int getKing(boolean isBlack) {
        for (int i = 0; i < board.length; i++) {
            if ((isBlack) ? board[i] == -6 : board[i] == 6) return i;
        }
        return 0;
    }

    public Stream<Move> ponyMoves(int i, boolean isBlack) {
        return ponyMoves(i, isBlack, board);
    }

    Stream<Move> ponyMoves(int i, boolean isBlack, int[] board) {
        Stream.Builder<Move> sb = Stream.builder();
        sb.add(getPonyMove(i, 1, 12, isBlack, board[i], board));
        sb.add(getPonyMove(i, 1, -12, isBlack, board[i], board));
        sb.add(getPonyMove(i, -1, 12, isBlack, board[i], board));
        sb.add(getPonyMove(i, -1, -12, isBlack, board[i], board));
        sb.add(getPonyMove(i, 12, 1, isBlack, board[i], board));
        sb.add(getPonyMove(i, 12, -1, isBlack, board[i], board));
        sb.add(getPonyMove(i, -12, 1, isBlack, board[i], board));
        sb.add(getPonyMove(i, -12, -1, isBlack, board[i], board));
        return sb.build().filter(Objects::nonNull);
    }

    Move getPonyMove(int start, int dir1, int dir2, boolean isBlack, int pice, int[] board) {
        return ((board[start + dir1 + dir1 + dir2] == 0 || ((isBlack) ? board[start + dir1 + dir1 + dir2] > 0 : board[start + dir1 + dir1 + dir2] < 0)) && board[start + dir1 + dir1 + dir2] != 9) ? new Move(start, start + dir1 + dir1 + dir2, pice) : null;
    }

    Move getPonyMove(int start, int dir1, int dir2, boolean isBlack, int pice) {
        return getPonyMove(start, dir1, dir2, isBlack, pice, board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            if(board[i]!=9) sb.append(board[i]);
            if ((i + 1) % 12 == 0) sb.append('\n');
        }
        return sb.toString();
    }


    private boolean move(Move move, int[] board) {
        board[move.from()] = 0;
        board[move.to()] = move.pice();
        return true;
    }

    public boolean move(Move move) {
        if (isWhite == move.pice() < 0 || getMoves(move.from()).noneMatch(e -> e.to() == move.to())) {
            System.out.println("Move not Possible" + move);
            return false;
        }
        if (move.from() == 26) movedPices[0] = true;
        if (move.from() == 110) movedPices[3] = true;
        if (move.from() == 117) movedPices[5] = true;
        if (move.from() == 33) movedPices[2] = true;
        if (move.from() == 114) movedPices[4] = true;
        if (move.from() == 30) movedPices[1] = true;
        if (move.from() == 30) {
            if (move.to() == 28) {
                board[26] = 0;
                board[29] = -2;
                board[30] = 0;
                board[28] = -6;
                isWhite = !isWhite;
                isCheckMate();
                history.add(new SaveingPoint(Arrays.copyOf(board, board.length), isWhite, Arrays.copyOf(movedPices, movedPices.length)));
                return true;

            } else if (move.to() == 32) {
                board[33] = 0;
                board[31] = -2;
                board[30] = 0;
                board[32] = -6;
                isWhite = !isWhite;
                isCheckMate();
                history.add(new SaveingPoint(Arrays.copyOf(board, board.length), isWhite, Arrays.copyOf(movedPices, movedPices.length)));
                return true;
            }
        }
        if (move.from() == 114) {
            if (move.to() == 112) {
                board[110] = 0;
                board[113] = 2;
                board[114] = 0;
                board[112] = 6;
                isWhite = !isWhite;
                isCheckMate();
                history.add(new SaveingPoint(Arrays.copyOf(board, board.length), isWhite, Arrays.copyOf(movedPices, movedPices.length)));
                return true;

            } else if (move.to() == 116) {
                board[117] = 0;
                board[115] = 2;
                board[114] = 0;
                board[116] = 6;
                isWhite = !isWhite;
                isCheckMate();
                history.add(new SaveingPoint(Arrays.copyOf(board, board.length), isWhite, Arrays.copyOf(movedPices, movedPices.length)));
                return true;
            }
        }
        board[move.from()] = 0;
        board[move.to()] = move.pice();
        isWhite = !isWhite;
        isCheckMate();
        history.add(new SaveingPoint(Arrays.copyOf(board, board.length), isWhite, Arrays.copyOf(movedPices, movedPices.length)));
        return true;
    }
}
