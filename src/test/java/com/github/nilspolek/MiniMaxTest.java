package com.github.nilspolek;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniMaxTest {
    @Test
    void findBestMove(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        System.out.println(b.findBestMove(true));
    }
    @Test
    void findBestMoveTestEndgamePuzzle(){
        Board b = new Board();
        b.setFEN("k7/ppp5/8/8/8/8/3r4/5R1K");
        System.out.println(b.findBestMove(false));
    }

}