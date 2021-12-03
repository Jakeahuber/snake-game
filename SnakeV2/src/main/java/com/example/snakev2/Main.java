package com.example.snakev2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        int screenWidth = 615;
        int screenHeight = 525;
        int squareSize = 15;
        int snakeSpeed = 3;

        // Screen setup
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        Canvas canvas = new Canvas(screenWidth, screenHeight);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Detects pressed key
        String[] previousInput = new String[1];
        ArrayList<String> input = new ArrayList<>();
        scene.setOnKeyPressed(
                e -> {
                    String code = e.getCode().toString();

                    if (!input.contains(code)) {
                        input.add(code);
                    }
                    if(input.size() != 1) {
                        previousInput[0] = input.get(0);
                        input.remove(0);
                    }
                }
        );

        // Creates snake
        ArrayList<Square> snake = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < 15; i++) {
            snake.add(new Square("green-square.png", 300 - counter, 255));
            counter += squareSize;
        }
        for (int i = 0; i < snake.size(); i++) {
            gc.drawImage(snake.get(i).getImage(), snake.get(i).getX1(), snake.get(i).getY1());
        }
        Square snakeHead = snake.get(0);

        // Creates white square with random location
        ArrayList<Square> whiteSquares = new ArrayList<>();
        int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        whiteSquares.add(new Square("white-square.png", coordinates[0], coordinates[1]));
        gc.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX1(), whiteSquares.get(0).getY1());

        // blackImage is used to make the snake appear that it is moving
        Image blackImage = new Image("black-square.png", squareSize, squareSize, false, false);

        new AnimationTimer() {
            public void handle(long currentNanoTime)
            {
                if (input.contains("W")) {
                    int distance = -snakeSpeed;
                    if (previousInput[0].equals("S")) {
                        distance = snakeSpeed;
                    }
                    for (Square value : snake) {
                        value.moveY(distance);
                    }
                }
                else if (input.contains("A")) {

                    int distance = -snakeSpeed;
                    // Snake cannot move backwards
                    if (previousInput[0].equals("D")) {
                        distance = snakeSpeed;
                    }
                    for (Square value : snake) {
                        value.moveX(distance);
                    }
                }
                else if (input.contains("S")) {

                    int distance = snakeSpeed;
                    // Snake cannot move backwards
                    if (previousInput[0].equals("W")) {
                        distance = -snakeSpeed;
                    }
                    for (Square value : snake) {
                        value.moveY(distance);
                    }
                }
                else if (input.contains("D")) {
                    int distance = snakeSpeed;
                    // Snake cannot move backwards
                    if (previousInput[0].equals("A")) {
                        distance = -snakeSpeed;
                    }
                    for (Square value : snake) {
                        value.moveX(distance); // adds 3 to currentX
                    }
                }

                if (Math.abs(snakeHead.getCurrentX() - snakeHead.getX1()) == snakeSpeed ||
                    Math.abs(snakeHead.getCurrentY() - snakeHead.getY1()) == snakeSpeed) {
                    GameManager.moveSnake(snakeHead, snake, gc, blackImage);
                }

                if (snakeHead.collision(whiteSquares.get(0))) {
                    System.out.println("Direction: " + input.get(0));

                    if (input.get(0) == "W" || input.get(0) == "S") {
                        // keep same x, get previous y
                        snake.add(new Square("green-square.png", snake.get(snake.size() -1).getX1(), snake.get(snake.size() -1).getPreviousY()));
                    }
                    else {
                        // keep same y, get previous x
                        snake.add(new Square("green-square.png", snake.get(snake.size() -1).getPreviousX(), snake.get(snake.size() -1).getY1()));
                    }


                    System.out.println("White square: X: " + whiteSquares.get(0).getX1() + ", Y: " + whiteSquares.get(0).getY1());
                    System.out.println("X: " + snake.get(snake.size() -1).getPreviousX() + ". Y: " + snake.get(snake.size() -1).getPreviousY());
                    whiteSquares.remove(0);

                    int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);

                    whiteSquares.add(new Square("white-square.png", coordinates[0], coordinates[1]));

                    gc.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX1(), whiteSquares.get(0).getY1());

                    // make the white square part of the snake
                }
            }
        }.start();

        stage.setTitle("Snake");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}