package com.github.nilspolek;

import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {
    @Test
    void testEvaluate(){
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/8/3r4/5R1K");
        System.out.println(b.evaluate());
    }
    @Test
    void testIsCheckmate(){
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/8/8/4R2K");
        assertFalse(b.isCheckMate() != 0);
        b.move(new Move(114,30,2));
        assertTrue(b.isCheckMate() != 0);
    }
    @Test
    void undoMoves(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        var test = Arrays.copyOf(b.board,b.board.length);
        var test2 = Arrays.copyOf(b.movedPices,b.movedPices.length);
        var test3 = b.isWhite;
        var test4 = Arrays.copyOf(b.getAllMoves().toArray(),b.getAllMoves().toArray().length);
        b.move(new Move(100,88,1));
        b.board = b.history.get(0).board();
        b.movedPices =b.history.get(0).movedPices();
        b.isWhite = b.history.get(0).isWhite();

        assertArrayEquals(test, b.history.get(0).board());
        assertArrayEquals(test2, b.history.get(0).movedPices());
        assertEquals(test3,b.history.get(0).isWhite());
        assertArrayEquals(test4,b.getAllMoves().toArray());
    }
    @Test
    void undoMoves2(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        var test = Arrays.copyOf(b.board,b.board.length);
        var test2 = Arrays.copyOf(b.movedPices,b.movedPices.length);
        var test3 = b.isWhite;
        var test4 = Arrays.copyOf(b.getAllMoves().toArray(),b.getAllMoves().toArray().length);
        b.move(new Move(100,88,1));
        b.undoMove();

        assertArrayEquals(test, b.history.get(0).board());
        assertArrayEquals(test2, b.history.get(0).movedPices());
        assertEquals(test3,b.history.get(0).isWhite());
        assertArrayEquals(test4,b.getAllMoves().toArray());
    }
    @Test
    void testUndoMoves() {
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        b.move(new Move(100, 88, 1));
        b.move(new Move(38, 50, -1));
        b.undoMove();
        System.out.println(b);
        Board c = new Board();
        c.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        var s = c.getAllMoves();
        c.move(new Move(100, 88, 1));
        c.move(new Move(38, 50, -1));
        c.undoMove();
        c.undoMove();
        assertEquals(s.count(), c.getAllMoves().count());
        b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

    }
    @Test
    void testGetAllMoves(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        b.getAllMoves().forEach(System.out::println);
    }

    @Test
    void undoMove() {
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        var test = Arrays.copyOf(b.getBoard(),b.getBoard().length);
        for (Move move : b.getAllMoves().toArray(Move[]::new)) {
            b.move(move);
            b.undoMove();
        }
    }

    @Test
    void testGetMovesWithoutCheckInkBoard() {
        Board b = new Board();
        b.setFEN("r7/8/8/8/8/8/R7/K5p1");
        Board c = new Board();
        c.setFEN("r7/8/8/p7/8/8/R7/K7");
        c.getMovesWithoutCheckExcludingKing(26).forEach(System.out::println);
    }

    @Test
    void isControlled() {
        Board b = new Board();
        b.setFEN("r7/8/8/7p/8/R7/8/K7");
        Board c = new Board();
        c.setFEN("r7/8/8/7p/8/2R5/8/K7");
        assertTrue(c.isControlled(110, true));
        assertTrue(b.isControlled(110, true, c.board));

    }

    @Test
    void testGetMoves() {
        Board b = new Board();
        b.setFEN("r7/8/8/8/8/8/2R5/K5p1");
        System.out.println(b.board[100]);
        b.getMoves(100).forEach(System.out::println);
        assertTrue(b.getMoves(100).anyMatch(e -> e.to() == 98));
        assertEquals(1, b.getMoves(100).count());
    }

    @Test
    void testGetKing() {
        Board b = new Board();
        b.setFEN("k7/8/8/8/8/3P4/8/3N3K");
        assertEquals(26, b.getKing(true));
        assertEquals(117, b.getKing(false));
    }

    @Test
    void testGetMovesWithoutCheck() {
        Board b = new Board();
        b.setFEN("8/1q6/8/8/8/8/8/Q7");
        b.getMovesWithoutCheckExcludingKing(110).forEach(System.out::println);
        assertEquals(21, b.getMovesWithoutCheckExcludingKing(110).count());
        assertEquals(23, b.getMovesWithoutCheckExcludingKing(39).count());
    }

    @Test
    void testIsControlled() {
        Board b = new Board();
        b.setFEN("8/q7/8/8/8/k7/8/Q2n4");
        assertFalse(b.isControlled(26, false));
        assertTrue(b.isControlled(38, false));
        assertFalse(b.isControlled(117, false));
        assertTrue(b.isControlled(113, false));
        assertTrue(b.isControlled(103, true));
        assertFalse(b.isControlled(104, true));
        b.setFEN("r7/8/8/8/8/8/R7/K5p1");
        assertFalse(b.isControlled(110, true));
    }

    @Test
    void testToString() {
        Board b = new Board();
        System.out.println(b);
    }

    @Test
    void testSetFEN() {
        Board b = new Board();
        b.setFEN("k7/8/8/8/8/8/8/RQKNB3");
        System.out.println(b);
    }

    @Test
    void testGetPice() {
        Board b = new Board().setFEN("8/k7/8/8/8/K7/4p1P1/8");
        System.out.println(b.getPice(104));
    }

    @Test
    void testGetLineFields() {
        Board b = new Board();
        System.out.println(b.board[26]);
        b.getLineFields(26, 1, true, 4).forEach(System.out::println);
    }
}