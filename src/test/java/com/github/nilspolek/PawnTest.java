package com.github.nilspolek;

import org.junit.jupiter.api.Test;


class PawnTest {

    @Test
    void pawnMoves() {
        Board b = new Board().setFEN("8/k7/8/8/8/K7/4p1P1/8");
        b.pawnMoves(104,true).forEach(e -> System.out.println(b.getPice(104)+" From: "+e.from()+" To: "+e.to()));
        b.pawnMoves(102,false).forEach(e -> System.out.println(b.getPice(102)+" From: "+e.from()+" To: "+e.to()));
        Board c = new Board();
        c.enpesentable=111;
        Board d = new Board();
        d.setFEN("8/P5k1/8/8/8/8/5K2/8");
        d.setWhite(true);
        d.getAllMoves().forEach(System.out::println);
        d.move(new Move(38,26,5));
        System.out.println(d.board[50]);
    }
}