package com.example.snakev2;

import javafx.scene.image.Image;

public class WhiteSquare {
    private double x, y;
    private Image image;

    public WhiteSquare(String imageFilePath, double x, double y) {
        image = new Image(imageFilePath, 50, 50, false, false);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getX2() {
        return x + image.getWidth();
    }
    public double getY2() {
        return y + image.getHeight();
    }

    public Image getImage() {
        return image;
    }
}
