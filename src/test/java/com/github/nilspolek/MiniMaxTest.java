package com.github.nilspolek;

import org.junit.jupiter.api.Test;

class MiniMaxTest {
    @Test
    void findBestMove(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        System.out.println(b.findBestMove(true));
    }
    @Test
    void fiedMateIn2(){
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/3r4/8/1K5R");
        System.out.println(b.findBestMove(true));
        Board c = new Board();
        c.setFEN("3r3k/8/8/8/8/8/4RPPP/7K");
        c.setWhite(false);
        System.out.println(c.findBestMove(false));
    }

}