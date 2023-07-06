package com.github.nilspolek;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxTest {
    @Test
    void findBestMove(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        System.out.println(b.findBestMove(true,2,false));
    }
/*    @Test
    void fiedMateIn2(){
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/3r4/8/1K5R");
        Move m1 = b.findBestMove(true,false);
        assertEquals(m1.from(),117);
        assertEquals(m1.to(),33);
        Board c = new Board();
        c.setFEN("1k5r/8/3R4/8/8/8/PPP5/K7");
        c.setWhite(false);
        Move m2 = c.findBestMove(true,true);
        assertEquals(m2.to(),117);
        assertEquals(m2.from(),33);
    }*/
    @Test
    void findMateIn1(){
        Board b = new Board();
        b.setFEN("k4r2/ppp5/8/8/8/8/PPP5/K7");
        b.setWhite(false);
        Move m1 = b.findBestMove(true,2,true);
        assertEquals(m1.from(),31);
        assertEquals(m1.to(),115);
        Board c = new Board();
        c.setFEN("k7/ppp5/8/8/8/8/PPP5/K4R2");
        Move m2 = c.findBestMove(true,2,false);
        assertEquals(m2.from(),115);
        assertEquals(m2.to(),31);
    }

}