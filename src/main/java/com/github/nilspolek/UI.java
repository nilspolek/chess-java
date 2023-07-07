package com.github.nilspolek;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import java.io.File;
import java.io.IOException;

public class UI extends PApplet {
    Board board;
    String fileSelected;
    PImage bg;
    PImage selectedField;
    PShape[] shapes = new PShape[12];
    int currentPage = 1;
    boolean fileSelectedbol = false;

    //1 = landing page
    //2 = Load Game
    //3 = Play Against Bot
    //4 = Play Tournament
    public static void main(String[] args) {
        String[] appArgs = {"Chess"};
        UI mySketch = new UI();
        Board b = new Board();
//        b.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
//        b.move(new Move(100,88,1));
        b.saveGame("./test.json");
        b.loadGame("./test.json");
        System.out.println(b.getFEN());
        PApplet.runSketch(appArgs, mySketch);
    }

    @Override
    public void draw() {
        background(color(255, 255, 255));
        switch (currentPage) {
            case 1 -> landingPage();
            case 2 -> loadGame();
        }
    }

    public void mousePressed() {

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

    private void landingPage() {
        fill(color(0, 0, 0));
        textSize(50);
        text("Load game", 126, 125);
        text("Play Against Bot", 67, 250);
        text("Play Tournament", 60, 375);
        if (mousePressed && currentPage == 1) {
            if (mouseY > 250) {
                System.out.println("{test3}");
            } else if (mouseY > 125) {
                System.out.println("test2");
            } else if (mouseY < 125) {
                currentPage = 2;
                System.out.println("Test1");
            }
        }
    }
    private void board() {
        image(bg,0,0);
        if(board == null) {
            board = new Board();
        }
    }

    private void loadGame() {
        if (!fileSelectedbol) {
            selectInput("Datei auswählen:", "fileSelected");
            fileSelectedbol = true;
        }
    }

    public void fileSelected(File selection) {
        if (selection == null) {
            println("Window was closed or the user hit cancel.");
        } else {
            println("User selected " + selection.getAbsolutePath());
        }
    }
}
