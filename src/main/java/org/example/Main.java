package org.example;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class Main extends PApplet {
    PShape[] shapes = new PShape[12];
    PImage bg;
    PImage selectedField;

    public static void main(String[] args) {
        String[] appArgs = { "Chess" };
        Main mySketch = new Main();
        PApplet.runSketch(appArgs, mySketch);
    }
    public void settings() {
        size(500, 500);
    }
    public void setup() {
        noStroke();
        //        Black Pieces
        shapes[0] = loadShape("./pix/pieces/bB.svg");
        shapes[1] = loadShape("./pix/pieces/bK.svg");
        shapes[2] = loadShape("./pix/pieces/bN.svg");
        shapes[3] = loadShape("./pix/pieces/bP.svg");
        shapes[4] = loadShape("./pix/pieces/bQ.svg");
        shapes[5] = loadShape("./pix/pieces/bR.svg");
        //            White Pieces
        shapes[6] = loadShape("./pix/pieces/wB.svg");
        shapes[7] = loadShape("./pix/pieces/wK.svg");
        shapes[8] = loadShape("./pix/pieces/wN.svg");
        shapes[9] = loadShape("./pix/pieces/wP.svg");
        shapes[10] = loadShape("./pix/pieces/wQ.svg");
        shapes[11] = loadShape("./pix/pieces/wR.svg");
        bg = loadImage("./pix/board.jpg");
        selectedField = loadImage("./pix/isSelected.png");
    }
    @Override
    public void draw(){
        // Background
        image(bg, 0, 0, 500, 500);
        shape(shapes[0], 0 , 0, 62, 62);
    }

}