package org.example.logik;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void kingMoves() {
        Board b = new Board();
        b.setFEN("k7/8/8/8/8/8/8/8");
        b.kingMoves(26,true).forEach(System.out::println);
    }
}