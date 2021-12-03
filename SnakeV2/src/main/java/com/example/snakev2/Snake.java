package com.example.snakev2;

import javafx.scene.image.Image;

public class Snake {
    private Image image;
    private double x, y;
    private double currentX, currentY;
    private Image blackImage;

    public Snake(String imageFilePath, double x, double y, String blackImageFilePath) {
        image = new Image(imageFilePath, 50, 50, false, false);
        this.x = x;
        this.y = y;
        this.currentX = x;
        this.currentY = y;
        this.blackImage = new Image(blackImageFilePath, 50, 50, false, false);
    }

    public double getX1() { return x; }
    public double getX2() { return x + image.getWidth(); }
    public double getY1() { return y; }
    public double getY2() { return y + image.getHeight(); }

    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }

    public Image getImage() {return image; }
    public Image getBlackImage() { return blackImage; }

    public void moveX(int distance) {
        currentX += distance;
    }

    public void moveY(int distance) {
        currentY += distance;
    }

    public void resetX() {
        x = currentX;
    }

    public void resetY() {
        y = currentY;
    }

    public boolean collision(WhiteSquare whiteSquare) {
        boolean hit = false;
        if (this.getX2() > whiteSquare.getX() &&
                this.x < whiteSquare.getX2() &&
                this.getY2() > whiteSquare.getY() &&
                this.y < whiteSquare.getY2()) {
            hit = true;
        }
        return hit;
    }

    public void addLength() {

    }
}
