package com.github.nilspolek;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniMaxTest {
    @Test
    void findBestMove(){
        Board b = new Board();
        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        System.out.println(b.findBestMove(b.board, 2,true));
    }

}