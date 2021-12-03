package com.example.snakev2;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Square {
    private Image image;
    private double x, y;
    private double currentX, currentY;
    private double previousX, previousY;

    public Square(String imageFilePath, double x, double y) {
        image = new Image(imageFilePath, 15, 15, false, false);
        this.x = x;
        this.y = y;
        currentX = x;
        currentY = y;
        previousX = x;
        previousY = y;
    }

    public double getX1() { return x; }
    public double getX2() { return x + image.getWidth(); }
    public double getY1() { return y; }
    public double getY2() { return y + image.getHeight(); }

    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }
    public double getPreviousX() { return previousX; }
    public double getPreviousY() { return previousY; }

    public void setX(double location) { x = location; }

    public void setY(double location) { y = location; }

    public Image getImage() {return image; }

    public void setPreviousX(double input) {
        previousX = input;
    }

    public void setPreviousY(double input) {
        previousY = input;
    }

    public void moveX(int distance) {
        currentX += distance;
    }

    public void moveY(int distance) {
        currentY += distance;
    }

    public boolean collision(Square whiteSquare) {
        boolean hit = false;
        //if (this.getX2() > whiteSquare.getX1() &&
          //      this.x < whiteSquare.getX2() &&
            //    this.getY2() > whiteSquare.getY1() &&
              //  this.y < whiteSquare.getY2()) {
          //  hit = true;
        //}

        if (this.getX1() == whiteSquare.getX1() && this.getY1() == whiteSquare.getY1()) {
            hit = true;
        }

        return hit;
    }



}
