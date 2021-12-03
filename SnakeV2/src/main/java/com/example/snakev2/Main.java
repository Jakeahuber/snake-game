package com.example.snakev2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        int screenWidth = 600;
        int screenHeight = 500;
        int squareSize = 15;
        double[] snakeSpeed = {3};
        int[] score = {0};

        // Screen setup
        Group root = new Group();
        Scene mainScene = new Scene(root);
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("snake-icon.png"));

        Canvas canvas = new Canvas(screenWidth, screenHeight);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Detects pressed key
        String[] previousInput = new String[1];
        ArrayList<String> input = new ArrayList<>();

        Boolean[] firstMove = {true};
        mainScene.setOnKeyPressed(
                e -> {
                    String code = e.getCode().toString();

                    if (!input.contains(code)) {
                        // Doesn't include non-WASD input, unless it's the user's first keyboard input
                        if ((code.equals("W")) || (code.equals("A")) || (code.equals("S")) || (code.equals("D")) || firstMove[0]) {
                            input.add(code);
                            firstMove[0] = false;
                        }
                    }

                    if(input.size() != 1) {
                        previousInput[0] = input.get(0);
                        input.remove(0);
                    }
                }
        );

        // Creates snake
        ArrayList<GreenSquare> snake = new ArrayList<>();
        snake.add(new GreenSquare("green-square.png", 300, 255, squareSize));
        gc.drawImage(snake.get(0).getImage(), snake.get(0).getX(), snake.get(0).getY());
        GreenSquare snakeHead = snake.get(0);

        // Creates white square with random location
        ArrayList<WhiteSquare> whiteSquares = new ArrayList<>();
        int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        whiteSquares.add(new WhiteSquare("white-square.png", coordinates[0], coordinates[1], squareSize / 2));
        gc.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX(), whiteSquares.get(0).getY());

        // blackImage is used to make the snake appear that it is moving
        Image blackImage = new Image("black-square.png", squareSize, squareSize, false, false);

        new AnimationTimer() {
            public void handle(long currentNanoTime)
            {
                // Displays score in top right corner
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.fillText(Integer.toString(score[0]), screenWidth - 10, 20);

                // Gets user input and moves snake accordingly
                if (input.contains("W")) {
                    double distance = GameManager.getDistance(-snakeSpeed[0], previousInput[0], "S");
                    GameManager.moveSnake(snakeHead, snake, gc, blackImage, distance, "Y");
                }
                else if (input.contains("A")) {
                    double distance = GameManager.getDistance(-snakeSpeed[0], previousInput[0], "D");
                    GameManager.moveSnake(snakeHead, snake, gc, blackImage, distance, "X");
                }
                else if (input.contains("S")) {
                    double distance = GameManager.getDistance(snakeSpeed[0], previousInput[0], "W");
                    GameManager.moveSnake(snakeHead, snake, gc, blackImage, distance, "Y");
                }
                else if (input.contains("D")) {
                    double distance = GameManager.getDistance(snakeSpeed[0], previousInput[0], "A");
                    GameManager.moveSnake(snakeHead, snake, gc, blackImage, distance, "X");
                }

                // Snake makes contact with a white square
                if (snakeHead.collision(whiteSquares.get(0))) {
                    gc.drawImage(blackImage, whiteSquares.get(0).getX(), whiteSquares.get(0).getY());
                    if (input.get(0).equals("W") || input.get(0).equals("S")) {
                        double distance = GameManager.getDistance(snakeSpeed[0], input.get(0), "W");

                        for (int i = 0; i < 4; i++) {
                            snake.add(new GreenSquare("green-square.png", snake.get(snake.size() -1).getX(), snake.get(snake.size() -1).getPreviousY() + distance, squareSize));
                        }
                    }
                    if (input.get(0).equals("A") || input.get(0).equals("D")) {
                        double distance = GameManager.getDistance(snakeSpeed[0], input.get(0), "D");

                        for (int i = 0; i < 4; i++) {
                            snake.add(new GreenSquare("green-square.png", snake.get(snake.size() -1).getPreviousX() + distance, snake.get(snake.size() -1).getY(), squareSize));
                        }
                    }

                    whiteSquares.remove(0);
                    int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
                    whiteSquares.add(new WhiteSquare("white-square.png", coordinates[0], coordinates[1], squareSize / 2));
                    gc.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX(), whiteSquares.get(0).getY());

                    // Removes previous score by drawing a black square over it
                    gc.drawImage(new Image("black-square.png", 100, 15, false, false), screenWidth- 100, 8);

                    score[0]++;
                }

                // Snake collides into itself
                for (int i = 10; i < snake.size(); i++) {
                    if (snakeHead.collision(snake.get(i))) {
                        System.out.println("Game over!");
                    }
                }

                // Increase snake speed every 5 points
                if (score[0] % 5 == 0 && score[0] != 0) {
                    snakeSpeed[0] += 0.003;
                }

                //todo: increase snake speed with every 5 scores?
            }
        }.start();

        stage.setTitle("Snake");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}