package com.example.snakev2;

public class GreenSquare extends Square {

    private double previousX, previousY;

    public GreenSquare(String imageFilePath, double x, double y, int squareSize) {
        super(imageFilePath, x, y, squareSize);
        previousX = x;
        previousY = y;
    }

    public double getPreviousX() { return previousX; }
    public double getPreviousY() { return previousY; }

    public void setPreviousX(double input) {
        previousX = input;
    }
    public void setPreviousY(double input) {
        previousY = input;
    }


    public boolean collision(Square square) {
        boolean hit = false;
        if (this.getX2() > square.getX() &&
              this.getX() < square.getX2() &&
            this.getY2() > square.getY() &&
          this.getY() < square.getY2()) {
          hit = true;
        }
        //if (this.getX() == square.getX() && this.getY() == square.getY()) {
        //    hit = true;
        //}

        return hit;
    }

}
