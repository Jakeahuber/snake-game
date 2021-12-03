package com.example.snakev2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    public static int[] getRandomCoordinates(int screenWidth, int screenHeight, int squareSize, ArrayList<Square> snake) {
        int xCoordinate = chooseRandomCoordinate(screenWidth, squareSize, snake);
        int yCoordinate = chooseRandomCoordinate(screenHeight, squareSize, snake);

        int[] coordinates = new int[2];

        if (validRandomCoordinate(xCoordinate, yCoordinate, snake)) {
            coordinates[0] = xCoordinate;
            coordinates[1] = yCoordinate;
            return coordinates;
        }
        else {
            return getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        }
    }

    public static int chooseRandomCoordinate(int screenSize, int squareSize, ArrayList<Square> snake) {
        int options = screenSize / squareSize;
        Random random = new Random();
        int coordinate = random.nextInt(options) * 15;
        System.out.println(screenSize + ", coordinate: " + coordinate);
        return coordinate;
    }

    public static boolean validRandomCoordinate(int x, int y, ArrayList<Square> snake) {
        for (int i = 0; i < snake.size(); i++) {
            if (x == snake.get(i).getX1() || y == snake.get(i).getY1()) {
                return false;
            }
        }
        return true;
    }

    public static void moveSnake(Square snakeHead, ArrayList<Square> snake, GraphicsContext gc, Image blackImage) {
        moveSnakeHead(snakeHead, gc, blackImage);
        for (int i = 1; i < snake.size(); i++) {
            gc.drawImage(blackImage, snake.get(i).getX1(), snake.get(i).getY1());

            snake.get(i).setPreviousX(snake.get(i).getX1());
            snake.get(i).setPreviousY(snake.get(i).getY1());

            snake.get(i).setX(snake.get(i - 1).getPreviousX());
            snake.get(i).setY(snake.get(i - 1).getPreviousY());
            gc.drawImage(snake.get(i).getImage(), snake.get(i).getX1(), snake.get(i).getY1());
        }
    }

    public static void moveSnakeHead(Square snakeHead, GraphicsContext gc, Image blackImage) {
        gc.drawImage(blackImage, snakeHead.getX1(), snakeHead.getY1());
        snakeHead.setPreviousX(snakeHead.getX1());
        snakeHead.setX(snakeHead.getCurrentX());
        snakeHead.setPreviousY(snakeHead.getY1());
        snakeHead.setY(snakeHead.getCurrentY());
        gc.drawImage(snakeHead.getImage(), snakeHead.getX1(), snakeHead.getY1());
    }

}
