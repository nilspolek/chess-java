package com.github.nilspolek;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxTest {
    @Test
    void findBestMove(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        System.out.println(b.findBestMove(2,false,System.currentTimeMillis()+30_000));
    }
    @Test
    void fiedMateIn2() throws InterruptedException {
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/3r4/8/1K5R");
        b.findBestMove(3,900_000);
        while (b.isAlive()){
            Thread.sleep(1000);
        }
        Move m1 = b.lastBestMove;
        assertEquals(m1.from(),117);
        assertEquals(m1.to(),33);
        Board c = new Board();
        c.setFEN("1k5r/8/3R4/8/8/8/PPP5/K7");
        c.setWhite(false);
        c.findBestMove(4,900_000);
        while (c.isAlive()){
            Thread.sleep(1000);
        }
        Move m2 = c.lastBestMove;
        assertEquals(m2.to(),117);
        assertEquals(m2.from(),33);
    }
    @Test
    void findMateIn1() throws InterruptedException {
        Board b = new Board();
        b.setFEN("k4r2/ppp5/8/8/8/8/PPP5/K7");
        b.setWhite(false);
        Move m1 = b.findBestMove(2,true,System.currentTimeMillis()+30_000);
        assertEquals(m1.from(),31);
        assertEquals(m1.to(),115);
        Board c = new Board();
        c.setFEN("k7/ppp5/8/8/8/8/PPP5/K4R2");
        c.findBestMove(2,1000);
        while (c.isAlive()) Thread.sleep(1000);
        Move m2 = c.lastBestMove;
        assertEquals(m2.from(),115);
        assertEquals(m2.to(),31);
    }
    @Test
    void findMateIn1InAnotherThread() throws InterruptedException {
        Board b = new Board();
        b.setFEN("k4r2/ppp5/8/8/8/8/PPP5/K7");
        b.setWhite(false);
        b.TIME_LIMIT = 10000;
        b.start();
        while (b.isAlive()){
            Thread.sleep(1000);
            System.out.println("Waiting");
        }
        System.out.println(b.lastBestMove);
    }
}