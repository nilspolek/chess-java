package org.example.logik;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void kingMoves() {
        Board b = new Board();
        b.setFEN("k7/8/8/8/8/8/8/8");
        assertEquals(3,b.kingMoves(26,true).count());
        b.setFEN("k7/8/8/2B5/8/8/8/8");
        assertEquals(2,b.kingMoves(26,true).count());
    }
    @Test
    void testBlock(){
        Board b = new Board();
        b.setFEN("kr6/8/8/3B4/8/8/8/8");
        b.isWhite = false;
        b.getAllMoves().forEach(System.out::println);
        assertEquals(2,b.getAllMoves().count());
    }
    @Test
    void testBucks(){
        Board b = new Board();
        b.setFEN("4k3/6p1/8/7Q/8/8/8/4K3");
        b.isWhite = false;
        b.getAllMoves().forEach(System.out::println);
    }
}