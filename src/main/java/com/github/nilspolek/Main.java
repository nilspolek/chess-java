package com.github.nilspolek;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class Main extends PApplet {
    int clickedPice = 25;
    static Board b;
    PShape[] shapes = new PShape[12];
    PImage bg;
    PImage selectedField;

    static boolean playBot;

    public static void main(String... args) {
        String[] appArgs = {"Chess"};
        Main mySketch = new Main();
        b = new Board();
        playBot = false;
        if(args.length == 0)
            b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        else
            b.setFEN(args[0]);
        PApplet.runSketch(appArgs, mySketch);
    }

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        noStroke();
        //        Black Pieces
        shapes[0] = loadShape("./.app/pix/pieces/bB.svg");
        shapes[1] = loadShape("./.app/pix/pieces/bK.svg");
        shapes[2] = loadShape("./.app/pix/pieces/bN.svg");
        shapes[3] = loadShape("./.app/pix/pieces/bP.svg");
        shapes[4] = loadShape("./.app/pix/pieces/bQ.svg");
        shapes[5] = loadShape("./.app/pix/pieces/bR.svg");
        //            White Pieces
        shapes[6] = loadShape("./.app/pix/pieces/wB.svg");
        shapes[7] = loadShape("./.app/pix/pieces/wK.svg");
        shapes[8] = loadShape("./.app/pix/pieces/wN.svg");
        shapes[9] = loadShape("./.app/pix/pieces/wP.svg");
        shapes[10] = loadShape("./.app/pix/pieces/wQ.svg");
        shapes[11] = loadShape("./.app/pix/pieces/wR.svg");
        bg = loadImage("./.app/pix/board.jpg");
        selectedField = loadImage("./.app/pix/isSelected.png");
    }

    public void mousePressed() {
        int row = (int) (mouseY / 62.5);
        int col = (int) (mouseX / 62.5);
        if(b.board[row * 12 + 26 + col] < 0 && playBot)return;
        if(clickedPice != 25){
            if(b.move(new Move(clickedPice,(row * 12) + 26 + col,b.board[clickedPice])) && playBot) {
                clickedPice = 0;
                b.move(b.findBestMove(false,true));
            }
        }
        clickedPice = row * 12 + 26 + col;
    }

    @Override
    public void draw() {
        // Background
        image(bg, 0, 0, 500, 500);
        for (int i = 0; i < b.getBoard().length; i++) {
            switch (b.getBoard()[i]) {
                case 1 -> shape(shapes[9], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case 2 -> shape(shapes[11], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case 3 -> shape(shapes[8], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case 4 -> shape(shapes[6], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case 5 -> shape(shapes[10], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case 6 -> shape(shapes[7], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -1 -> shape(shapes[3], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -2 -> shape(shapes[5], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -3 -> shape(shapes[2], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -4 -> shape(shapes[0], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -5 -> shape(shapes[4], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
                case -6 -> shape(shapes[1], (float) ((i % 8) * 62.5), (float) ((i / 8) * 62.5), 62.5F, 62.5F);
            }
        }
        if(clickedPice > 25 && clickedPice < 118)
            b.getMoves(clickedPice).forEach(e -> image(selectedField, (float) (((e.to() - 2) % 12) * 62.5) + 10, (float) (((e.to() - 26) / 12) * 62.5) + 10, 42.5F, 42.5F));
    }

}