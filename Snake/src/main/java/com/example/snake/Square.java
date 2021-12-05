package com.example.snake;

import javafx.scene.image.Image;

public class Square {
    private final Image image;
    private double x, y;

    public Square(String imageFilePath, double x, double y, int squareSize) {
        image = new Image(imageFilePath, squareSize, squareSize, false, false);
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double location) { x = location; }
    public void setY(double location) { y = location; }

    public Image getImage() {return image; }

    public double getX2() {
        return x + image.getWidth();
    }

    public double getY2() {
        return y + image.getHeight();
    }


}
