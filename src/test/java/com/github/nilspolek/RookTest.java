package com.github.nilspolek;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    @Test
    void rookMoves() {
        Board b = new Board().setFEN("8/8/8/8/8/8/8/R7");
        b.board[116] = -1;
        b.rookMoves(110,false).forEach(System.out::println);
        assertEquals(b.rookMoves(110,false).count(),13);
        assertTrue(b.rookMoves(110,false).anyMatch(e -> e.to() == 26));
        assertTrue(b.rookMoves(110,false).anyMatch(e -> e.to() == 26));
        assertTrue(b.rookMoves(110,false).anyMatch(e -> e.to() == 116));
    }
}