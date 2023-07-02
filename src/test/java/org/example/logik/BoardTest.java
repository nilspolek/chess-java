package org.example.logik;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {
    @Test
    void  testGetMoves(){
        Board b = new Board();
        b.setFEN("8/1q6/8/8/8/8/8/Q7");
        b.getMoves(110).forEach(System.out::println);
        assertEquals(21,b.getMoves(110).count());
        assertEquals(23,b.getMoves(39).count());
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