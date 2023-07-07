package com.github.nilspolek;

import java.util.stream.Stream;

public interface Chessable {
    Stream<Move> getAllMoves();

    Stream<Move> pawnMoves(int i, boolean isBlack);
    Stream<Move> bishopMoves(int i, boolean isBlack);
    Stream<Move> ponyMoves(int i, boolean isBlack);
    Stream<Move> rookMoves(int i, boolean isBlack);
    Stream<Move> queenMoves(int i, boolean isBlack);
    Stream<Move> kingMoves(int i, boolean isBlack);
    int[] getBoard();
    void undoMove();
    void saveGame(String path);
    void loadGame(String path);
    void setWhite(boolean isWhite);
    int evaluate();
    Move findBestMove(boolean isBlack);
    Move findBestMove(int depth, boolean isBlack);
    int getValue(int pice);
    String getFEN();
    Board setFEN(String FEN);
    char getPice(int i);
    char getPiceFromPice(int i);
    boolean isControlled(int field, boolean byBlack);
    Stream<Move> getMoves(int field);
    int isCheckMate();
    int getKing(boolean isBlack);
}
