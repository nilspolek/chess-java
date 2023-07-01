package org.example.logik;

import org.junit.jupiter.api.Test;

class PonyTest {

    @Test
    void getPonyMoves() {
        Board b = new Board();
        b.board[66] = 1;
        b.board[38] = -1;
        b.setFEN("8/8/2N5/8/8/8/8/8");
        System.out.println(b.board[52]);
        b.ponyMoves(52,false).forEach(System.out::println);
    }
    @Test
    void getPonyMove() {
        Board b = new Board();
        b.setFEN("8/8/2N5/8/8/8/8/8");
        System.out.println(b.getPonyMove(52,1,12,true,b.board[52]));
    }
}