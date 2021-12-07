package com.example.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {

    // Returns a valid x and y coordinate for spawning a new white square
    public static int[] getRandomCoordinates(int screenWidth, int screenHeight, int squareSize, ArrayList<GreenSquare> snake) {
        Random random = new Random();
        int xCoordinate = random.nextInt(screenWidth - 10);
        int yCoordinate = random.nextInt(screenHeight - 10);

        int[] coordinates = new int[2];

        if (validRandomCoordinate(xCoordinate, yCoordinate, snake, screenWidth, screenHeight, squareSize)) {
            coordinates[0] = xCoordinate;
            coordinates[1] = yCoordinate;
            return coordinates;
        }
        else {
            return getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        }
    }

    // Checks if the white square is in the same location as the snake and if it's too close to the counter
    public static boolean validRandomCoordinate(int x, int y, ArrayList<GreenSquare> snake, int screenWidth, int screenHeight, int squareSize) {
        Square whiteSquareTest = new Square("white-square.png", x, y, squareSize);
        for (GreenSquare greenSquare : snake) {
            if (greenSquare.collision(whiteSquareTest)) {
                return false;
            }

            if (x >= screenWidth - 100 && y <= 25) {
                return false;
            }

            if (x < 5 || x >= screenWidth - 5 || y < 5 || y >= screenHeight - 5) {
                return false;
            }
        }
        return true;
    }

    // Moves each square of the snake to the previous location of the square in front of the square.
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

    // Moves the first snake image and sets a previous location for it
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

    // Returns the correct direction and distance the snake should move
    public static double getDistance(double snakeSpeed, String input, String oppositeDirection) {
        double distance = snakeSpeed;
        if (input != null && input.equals(oppositeDirection)) {
            distance = -snakeSpeed;
        }
        return distance;
    }

    // Snake cannot move in the opposite direction.
    public static void checkOppositeDirection(ArrayList<String> input, String[] previousInput) {
        if (previousInput[0] != null) {
            if (input.contains("W") && previousInput[0].equals("S")) {
                input.add("S");
                input.remove(0);
            }
            if (input.contains("S") && previousInput[0].equals("W")) {
                input.add("W");
                input.remove(0);
            }
            if (input.contains("D") && previousInput[0].equals("A")) {
                input.add("A");
                input.remove(0);
            }
            if (input.contains("A") && previousInput[0].equals("D")) {
                input.add("D");
                input.remove(0);
            }
        }
    }

    // Removes previous white square and spawns a new one
    public static void resetWhiteSquare(ArrayList<Square> whiteSquares, int screenWidth, int screenHeight, int squareSize,
                                        ArrayList<GreenSquare> snake, GraphicsContext gcMain) {
        whiteSquares.remove(0);
        int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        whiteSquares.add(new Square("white-square.png", coordinates[0], coordinates[1], squareSize / 2));
        gcMain.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX(), whiteSquares.get(0).getY());
    }

    // After the player loses, the game is reset back to its original state
    public static void resetGame(GreenSquare[] snakeHead, ArrayList<GreenSquare> snake, int squareSize, int[] score, double[] snakeSpeed,
                                 ArrayList<String> input, String[] previousInput, Boolean[] firstMove, double originalSnakeSpeed,
                                 Boolean[] firstScore, GraphicsContext gcMain, Image blackImage, int screenWidth, int screenHeight, ArrayList<Square> whiteSquares) {

        // Reset snake arraylist
        for (GreenSquare greenSquare : snake) {
            gcMain.drawImage(blackImage, greenSquare.getX(), greenSquare.getY());
        }
        snake.clear();
        snake.add(new GreenSquare("green-square.png", 300, 255, squareSize));
        snakeHead[0] = snake.get(0);
        gcMain.drawImage(snakeHead[0].getImage(), snakeHead[0].getX(), snakeHead[0].getY());

        // Reset score
        gcMain.drawImage(new Image("black-square.png", 100, 15, false, false), screenWidth - 100, 8);
        score[0] = 0;

        // Reset speed
        snakeSpeed[0] = originalSnakeSpeed;

        // Reset input
        input.clear();
        previousInput[0] = null;

        // Reset firstMove and firstScore back to true
        firstMove[0] = true;
        firstScore[0] = true;

        // Reset white square
        gcMain.drawImage(blackImage, whiteSquares.get(0).getX(), whiteSquares.get(0).getY());
        GameManager.resetWhiteSquare(whiteSquares, screenWidth, screenHeight, squareSize, snake, gcMain);
    }

}
