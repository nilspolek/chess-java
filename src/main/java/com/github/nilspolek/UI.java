package com.github.nilspolek;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import java.io.File;
import java.util.Random;

public class UI extends PApplet {
    boolean save = false;
    boolean playerIsWhite;
    boolean botThinks = false;
    boolean playBot = true;
    int clickedPice = 0;
    Board board;
    File fileSelected;
    PImage bg;
    PImage selectedField;
    PShape[] shapes = new PShape[12];
    int currentPage = 1;
    boolean fileSelectedbol = false;

    boolean isWhite;

    //1 = landing page
    //2 = Load Game
    //3 = Play Against Bot
    //4 = Play Tournament
    public static void main(String[] args) {
        String[] appArgs = {"Chess"};
        UI mySketch = new UI();
        PApplet.runSketch(appArgs, mySketch);
    }

    @Override
    public void draw() {
        background(color(255, 255, 255));
        switch (currentPage) {
            case 1 -> landingPage();
            case 2 -> loadGame();
            case 3 -> playBot();
            case 4 -> duell();
        }
    }

    public void keyPressed() {
        if (board != null && (keyCode == 's' || keyCode == 'S')) {
            save = true;
            selectOutput("Select a file to write to:", "fileSelected",new File("Chess.txt"));
        }
        if (board != null && (keyCode == 'z' || keyCode == 'Z')) {
            board.undoMove();
        }
        if (board != null && (keyCode == 'y' || keyCode == 'Y')) {
            board.stopProcessing();
        }
        if (board != null && (keyCode == 'x' || keyCode == 'X')) {
            board = new Board();
            board.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        }
    }

    public void mousePressed() {
        if (board != null) {
            int row = (int) (mouseY / 62.5);
            int col = (int) (mouseX / 62.5);
            Move m = board.getAllMoves().filter(e -> (e.from()==clickedPice && e.to()==(row * 12) + 26 + col)).findFirst().orElse(null);
            if (!botThinks && m != null && board.move(m) && playBot) {
                thread("findBestMove");
            }
            clickedPice = row * 12 + 26 + col;
        }
    }

    public void findBestMove() {
        botThinks = true;
        board.move(board.findBestMove(1, playerIsWhite, System.currentTimeMillis() + 30_000));
        botThinks = false;
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
        text("Play Duel", 144, 375);
        if (mousePressed && currentPage == 1) {
            if (mouseY > 250) {
                currentPage = 4;
            } else if (mouseY > 125) {
                currentPage = 3;
            } else if (mouseY < 125) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentPage = 2;
            }
        }
    }

    private void duell() {
        playBot = false;
        board();
    }

    private void afterWonGame() {
        fill(color(0, 0, 0));
        textSize(50);
        text("Win", 10, 275);
    }

    private void playBot() {
        playBot = true;
        if (board == null) playerIsWhite = new Random().nextBoolean();
        board();
    }

    private void board() {
        image(bg, 0, 0, 500, 500);
        if (board == null) {
            board = new Board();
            board.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
            if (playBot && !playerIsWhite) thread("findBestMove");
        }
        for (int i = 0; i < board.getBoard().length; i++) {
            switch (board.getBoard()[i]) {
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
        if (clickedPice > 25 && clickedPice < 118)
            board.getMoves(clickedPice).forEach(e -> image(selectedField, (float) (((e.to() - 2) % 12) * 62.5) + 10, (float) (((e.to() - 26) / 12) * 62.5) + 10, 42.5F, 42.5F));
    }

    private void loadGame() {
        if (!fileSelectedbol) {
            selectInput("Datei auswählen:", "fileSelected");
            currentPage = 3;
            fileSelectedbol = true;
        }

    }

    public void fileSelected(File selection) {
        if (selection == null) {
            println("Window was closed or the user hit cancel.");
        } else {
            fileSelected = selection;
            if (save) {
                board.saveGame(selection.getAbsolutePath());
                exit();
            } else {
                board = new Board();
                board.loadGame(selection.getAbsolutePath());
            }
            ;
        }
    }
}
