package org.example.logik;

import org.junit.jupiter.api.Test;
class BoardTest {
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