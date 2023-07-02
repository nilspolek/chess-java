package org.example.logik;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {
    @Test
    void testGetMovesWithoutCheckInkBoard(){
        Board b = new Board();
        b.setFEN("r7/8/8/8/8/8/R7/K5p1");
        Board c = new Board();
        c.setFEN("r7/8/8/p7/8/8/R7/K7");
        c.getMovesWithoutCheck(26).forEach(System.out::println);
    }
    @Test
    void isControlled(){
        Board b = new Board();
        b.setFEN("r7/8/8/7p/8/R7/8/K7");
        Board c = new Board();
        c.setFEN("r7/8/8/7p/8/2R5/8/K7");
        assertTrue(c.isControlled(110,true));
        assertTrue(b.isControlled(110,true,c.board));

    }
    @Test
    void testGetMoves(){
        Board b = new Board();
        b.setFEN("r7/8/8/8/8/8/2R5/K5p1");
        System.out.println(b.board[100]);
        b.getMoves(100).forEach(System.out::println);
        assertTrue(b.getMoves(100).anyMatch(e -> e.to() == 98));
        assertEquals(1, b.getMoves(100).count());
    }
    @Test
    void testGetKing(){
        Board b = new Board();
        b.setFEN("k7/8/8/8/8/3P4/8/3N3K");
        assertEquals(26,b.getKing(true));
        assertEquals(117,b.getKing(false));
    }
    @Test
    void  testGetMovesWithoutCheck(){
        Board b = new Board();
        b.setFEN("8/1q6/8/8/8/8/8/Q7");
        b.getMovesWithoutCheck(110).forEach(System.out::println);
        assertEquals(21,b.getMovesWithoutCheck(110).count());
        assertEquals(23,b.getMovesWithoutCheck(39).count());
    }
    @Test
    void testIsControlled(){
        Board b = new Board();
        b.setFEN("8/q7/8/8/8/k7/8/Q2n4");
        assertFalse(b.isControlled(26,false));
        assertTrue(b.isControlled(38,false));
        assertFalse(b.isControlled(117,false));
        assertTrue(b.isControlled(113,false));
        assertTrue(b.isControlled(103,true));
        assertFalse(b.isControlled(104,true));
        b.setFEN("r7/8/8/8/8/8/R7/K5p1");
        assertFalse(b.isControlled(110,true));
    }
    @Test
    void testToString(){
        Board b = new Board();
        System.out.println(b);
    }
    @Test
    void testSetFEN(){
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
        b.getLineFields(26,1,true,4).forEach(System.out::println);
    }
}