package com.github.nilspolek;

import org.junit.jupiter.api.Test;


class PawnTest {

    @Test
    void pawnMoves() {
        Board b = new Board().setFEN("8/k7/8/8/8/K7/4p1P1/8");
        b.pawnMoves(104,true).forEach(e -> System.out.println(b.getPice(104)+" From: "+e.from()+" To: "+e.to()));
        b.pawnMoves(102,false).forEach(e -> System.out.println(b.getPice(102)+" From: "+e.from()+" To: "+e.to()));
    }
}