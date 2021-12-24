package com.example.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        int screenWidth = 600;
        int screenHeight = 500;
        int squareSize = 15;
        double[] snakeSpeed = {3};
        double originalSnakeSpeed = snakeSpeed[0];
        int[] score = {0};

        // Screen setup
        AnchorPane paneWelcome = new AnchorPane();
        Scene welcomeScene = new Scene(paneWelcome, screenWidth, screenHeight);
        welcomeScene.getStylesheets().add("styles.css");

        AnchorPane paneLeaderboard = new AnchorPane();
        Scene leaderboardScene = new Scene(paneLeaderboard, screenWidth, screenHeight);
        leaderboardScene.getStylesheets().add("styles.css");

        AnchorPane paneNewHighScore = new AnchorPane();
        Scene newHighScoreScene = new Scene(paneNewHighScore, screenWidth, screenHeight);
        newHighScoreScene.getStylesheets().add("styles.css");

        Group mainRoot = new Group();
        Scene mainScene = new Scene(mainRoot);

        stage.setScene(welcomeScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("snake-icon.png"));

        Canvas mainCanvas = new Canvas(screenWidth, screenHeight);
        mainRoot.getChildren().add(mainCanvas);

        /* Reads leaderboard.csv and adds values to scoreHash Hashtable
           The key is the player's rank and the value is the corresponding PlayerScore object */
        Hashtable<Integer, PlayerScore> scoreHash = new Hashtable<>();
        try {
            FileReader fileReader = new FileReader("Snake/src/main/resources/leaderboard.csv");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] cs = line.split(",");
                PlayerScore player = new PlayerScore(cs[0], Integer.parseInt(cs[1]));
                scoreHash.put(Integer.parseInt(cs[2]), player);
            }
            reader.close();
        }
        catch (Exception ex) {
            System.out.println("Couldn't read file.");
        }

        ArrayList<PlayerScore> leaderboard = new ArrayList<>();
        PlayerScore.createLeaderboard(leaderboard, scoreHash);

        // Welcome scene
        Label welcomeText = new Label("Snake Game");
        Label inputType = new Label("Controls: W, A, S, and D keys");
        inputType.getStyleClass().add("inputType");

        Button btnPlayNow = new Button("Play now!");
        btnPlayNow.setOnAction((ActionEvent event) -> stage.setScene(mainScene));

        Button btnLeaderboard = new Button("Leaderboard");
        btnLeaderboard.setOnAction((ActionEvent event) -> {
                    paneLeaderboard.getChildren().clear();
                    PlayerScore.updateLeaderboard(leaderboard, paneLeaderboard, screenWidth, welcomeScene, stage);
                    stage.setScene(leaderboardScene);
                });
        btnLeaderboard.getStyleClass().add("btnLeaderboard");

        AnchorPane.setLeftAnchor(welcomeText, (screenWidth / 2.0) - 133);
        AnchorPane.setTopAnchor(welcomeText, 30.0);

        AnchorPane.setLeftAnchor(btnPlayNow, (screenWidth / 2.0) - 63);
        AnchorPane.setTopAnchor(btnPlayNow, 120.0);

        AnchorPane.setLeftAnchor(btnLeaderboard, (screenWidth / 2.0) - 63);
        AnchorPane.setTopAnchor(btnLeaderboard, 180.0);

        AnchorPane.setLeftAnchor(inputType, (screenWidth / 2.0) - 88);
        AnchorPane.setBottomAnchor(inputType, 20.0);

        paneWelcome.getChildren().addAll(welcomeText, btnPlayNow, btnLeaderboard, inputType);

        // Main scene
        GraphicsContext gcMain = mainCanvas.getGraphicsContext2D();
        gcMain.setFill(Color.BLACK);
        gcMain.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

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

                    // Gets value of previous input
                    if(input.size() != 1) {
                        previousInput[0] = input.get(0);
                        input.remove(0);
                    }
                }
        );

        // Creates snake
        ArrayList<GreenSquare> snake = new ArrayList<>();
        snake.add(new GreenSquare("green-square.png", 300, 255, squareSize));
        gcMain.drawImage(snake.get(0).getImage(), snake.get(0).getX(), snake.get(0).getY());
        GreenSquare[] snakeHead = {snake.get(0)};

        // Creates white square with random location
        ArrayList<Square> whiteSquares = new ArrayList<>();
        int[] coordinates = GameManager.getRandomCoordinates(screenWidth, screenHeight, squareSize, snake);
        whiteSquares.add(new Square("white-square.png", coordinates[0], coordinates[1], squareSize / 2));
        gcMain.drawImage(whiteSquares.get(0).getImage(), whiteSquares.get(0).getX(), whiteSquares.get(0).getY());

        // blackImage is used to make the snake appear that it is moving (clears snake's previous location)
        Image blackImage = new Image("black-square.png", squareSize, squareSize, false, false);

        Boolean[] firstScore = {true};

        new AnimationTimer() {
            public void handle(long currentNanoTime)
            {
                // Displays score in top right corner
                gcMain.setFill(Color.WHITE);
                gcMain.setTextAlign(TextAlignment.RIGHT);
                gcMain.fillText(Integer.toString(score[0]), screenWidth - 10, 20);

                // Game ends if snake goes out of bounds
                if (snakeHead[0].outOfBounds(screenWidth, screenHeight)) {
                    if (PlayerScore.newHighScore(score[0], scoreHash)) {
                        stage.setScene(newHighScoreScene);
                    }
                    else {
                        GameManager.resetGame(snakeHead, snake, squareSize, score, snakeSpeed, input, previousInput,
                                firstMove, originalSnakeSpeed, firstScore, gcMain, blackImage, screenWidth, screenHeight, whiteSquares);
                        stage.setScene(welcomeScene);
                    }
                }

                // Snake cannot move in opposite direction
                GameManager.checkOppositeDirection(input, previousInput);

                // Moves snake up, down, left, or right
                if (input.contains("W")) {
                    GameManager.moveSnake(snakeHead[0], snake, gcMain, blackImage, -snakeSpeed[0], "Y");
                }
                else if (input.contains("A")) {
                    GameManager.moveSnake(snakeHead[0], snake, gcMain, blackImage, -snakeSpeed[0], "X");
                }
                else if (input.contains("S")) {
                    GameManager.moveSnake(snakeHead[0], snake, gcMain, blackImage, snakeSpeed[0], "Y");
                }
                else if (input.contains("D")) {
                    GameManager.moveSnake(snakeHead[0], snake, gcMain, blackImage, snakeSpeed[0], "X");
                }

                // Snake makes contact with a white square
                if (snakeHead[0].collision(whiteSquares.get(0))) {
                    gcMain.drawImage(blackImage, whiteSquares.get(0).getX(), whiteSquares.get(0).getY());

                    // Snake grows by 10 the first time it scores, and 3 every other time.
                    int squaresToBeAdded = 3;
                    if (firstScore[0]) {
                        squaresToBeAdded = 10;
                        firstScore[0] = false;
                    }

                    // Adds onto the snake from either the top or bottom
                    if (input.get(0).equals("W") || input.get(0).equals("S")) {
                        double distance = GameManager.getDistance(snakeSpeed[0], input.get(0), "S");

                        for (int i = 0; i < squaresToBeAdded; i++) {
                            snake.add(new GreenSquare("green-square.png", snake.get(snake.size() - 1).getX(), snake.get(snake.size() -1).getPreviousY() + distance, squareSize));
                        }
                    }

                    // Adds onto the snake from either the left or right
                    if (input.get(0).equals("A") || input.get(0).equals("D")) {
                        double distance = GameManager.getDistance(snakeSpeed[0], input.get(0), "D");
                        for (int i = 0; i < squaresToBeAdded; i++) {
                            snake.add(new GreenSquare("green-square.png", snake.get(snake.size() - 1).getPreviousX() + distance, snake.get(snake.size() -1).getY(), squareSize));
                        }
                    }

                    GameManager.resetWhiteSquare(whiteSquares, screenWidth, screenHeight, squareSize, snake, gcMain);

                    // Clears previous score and increments by one
                    gcMain.drawImage(new Image("black-square.png", 100, 15, false, false), screenWidth- 100, 8);
                    score[0]++;

                    // Snake speed increases with every score
                    snakeSpeed[0] += 0.03;
                }

                // Game ends if snake collides into itself
                for (int i = 10; i < snake.size(); i++) {
                    if (snakeHead[0].collision(snake.get(i))) {
                        if (PlayerScore.newHighScore(score[0], scoreHash)) {
                            stage.setScene(newHighScoreScene);
                        }
                        else {
                            GameManager.resetGame(snakeHead, snake, squareSize, score, snakeSpeed, input, previousInput,
                                    firstMove, originalSnakeSpeed, firstScore, gcMain, blackImage, screenWidth, screenHeight, whiteSquares);
                            stage.setScene(welcomeScene);
                        }
                    }
                }
            }
        }.start();

        // New high score scene
        Label newHighScoreLabel = new Label("New High Score!");
        TextField usernameInput = new TextField();
        Label usernameLabel = new Label("Username: ");
        usernameLabel.getStyleClass().add("smallText");
        Label labelInvalidInput = new Label();
        labelInvalidInput.getStyleClass().add("smallText");

        Button submitBtn = new Button("Submit");
        submitBtn.getStyleClass().add("submitBtn");
        submitBtn.setOnAction((ActionEvent event) -> {
            try {
                String username = usernameInput.getText();
                if (username.equals("")) {
                    throw new CustomException("Please enter a username", labelInvalidInput);
                }
                if (username.length() > 20) {
                    throw new CustomException("Username may not exceed 20 characters", labelInvalidInput);
                }
                for (int i = 0; i < username.length(); i++) {
                    if (!Character.isLetterOrDigit(username.charAt(i))) {
                        throw new CustomException("Username may only contain letters or digits", labelInvalidInput);
                    }
                }
                PlayerScore player = new PlayerScore(username, score[0]);
                PlayerScore.updateTable(player, scoreHash);
                PlayerScore.createLeaderboard(leaderboard, scoreHash);
                PlayerScore.saveLeaderboard(leaderboard);

                GameManager.resetGame(snakeHead, snake, squareSize, score, snakeSpeed, input, previousInput,
                        firstMove, originalSnakeSpeed, firstScore, gcMain, blackImage, screenWidth, screenHeight, whiteSquares);
                stage.setScene(welcomeScene);
                usernameInput.setText("");

            } catch (CustomException ignored) {}
        });

        AnchorPane.setLeftAnchor(newHighScoreLabel, (screenWidth / 2.0) - 170);
        AnchorPane.setTopAnchor(newHighScoreLabel, 30.0);

        AnchorPane.setLeftAnchor(usernameLabel, 30.0);
        AnchorPane.setTopAnchor(usernameLabel, 120.0);

        AnchorPane.setLeftAnchor(usernameInput, (screenWidth / 2.0) - 170);
        AnchorPane.setTopAnchor(usernameInput, 120.0);

        AnchorPane.setLeftAnchor(labelInvalidInput, 30.0);
        AnchorPane.setTopAnchor(labelInvalidInput, 180.0);

        AnchorPane.setLeftAnchor(submitBtn, 30.0);
        AnchorPane.setTopAnchor(submitBtn, 220.0);

        paneNewHighScore.getChildren().addAll(newHighScoreLabel, usernameInput, usernameLabel, submitBtn,
                labelInvalidInput);

        stage.setTitle("Snake");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}