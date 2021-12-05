package com.example.snake;

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
        return this.getX2() > square.getX() &&
               this.getX() < square.getX2() &&
               this.getY2() > square.getY() &&
               this.getY() < square.getY2();
    }

    public boolean outOfBounds(int screenWidth, int screenHeight) {
        return getX() < 0 || getY() < 0 || getX() > screenWidth - getImage().getWidth() ||
               getY() > screenHeight - getImage().getWidth();
    }

}
