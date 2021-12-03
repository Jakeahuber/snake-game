package com.example.snakev2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    public static int[] getRandomCoordinates(int screenWidth, int screenHeight, int squareSize, ArrayList<GreenSquare> snake) {
        int xCoordinate = chooseRandomCoordinate(screenWidth, squareSize, snake);
        int yCoordinate = chooseRandomCoordinate(screenHeight, squareSize, snake);

        int[] coordinates = new int[2];

        if (validRandomCoordinate(xCoordinate, yCoordinate, snake, screenWidth)) {
            coordinates[0] = xCoordinate;
            coordinates[1] = yCoordinate;
            return coordinates;
        }
        else {
            return getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        }
    }

    public static int chooseRandomCoordinate(int screenSize, int squareSize, ArrayList<GreenSquare> snake) {
        int options = screenSize;
        Random random = new Random();
        int coordinate = random.nextInt(options);
        return coordinate;
    }

    // todo: check if same location as previous white square
    // todo: check if same location as position of snake
    public static boolean validRandomCoordinate(int x, int y, ArrayList<GreenSquare> snake, int screenWidth) {
        for (int i = 0; i < snake.size(); i++) {
            if (x == snake.get(i).getX() || y == snake.get(i).getY()) {
                return false;
            }

            // ensures white square isn't too close to the counter
            if (x >= screenWidth - 100 && y <= 25) {
                return false;
            }
        }
        return true;
    }

    public static void moveSnake(GreenSquare snakeHead, ArrayList<GreenSquare> snake, GraphicsContext gc, Image blackImage,
                                 double distance, String direction) {

        moveSnakeHead(snakeHead, gc, blackImage, distance, direction);
        for (int i = 1; i < snake.size(); i++) {
            gc.drawImage(blackImage, snake.get(i).getX(), snake.get(i).getY());

            snake.get(i).setPreviousX(snake.get(i).getX());
            snake.get(i).setPreviousY(snake.get(i).getY());

            snake.get(i).setX(snake.get(i - 1).getPreviousX());
            snake.get(i).setY(snake.get(i - 1).getPreviousY());
            gc.drawImage(snake.get(i).getImage(), snake.get(i).getX(), snake.get(i).getY());
        }
    }

    public static void moveSnakeHead(GreenSquare snakeHead, GraphicsContext gc, Image blackImage, double distance,
                                     String direction) {

        gc.drawImage(blackImage, snakeHead.getX(), snakeHead.getY());
        snakeHead.setPreviousX(snakeHead.getX());
        snakeHead.setPreviousY(snakeHead.getY());

        if (direction.equals("X")) {
            snakeHead.setX(snakeHead.getX() + distance);
        }
        else if (direction.equals("Y")) {
            snakeHead.setY(snakeHead.getY() + distance);
        }

        gc.drawImage(snakeHead.getImage(), snakeHead.getX(), snakeHead.getY());
    }

    public static double getDistance(double snakeSpeed, String input, String oppositeDirection) {
        double distance = snakeSpeed;

        // Snake cannot move backwards
        if (input != null && input.equals(oppositeDirection)) {
            distance = -snakeSpeed;
        }
        return distance;
    }

}
