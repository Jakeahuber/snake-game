package com.example.snake;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class PlayerScore {
    private final String name;
    private final int score;
    private int rank;

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public int getRank() { return rank; }

    public void setRank(int rank) {
        this.rank = rank;
    }

    // Creates leaderboard scene with updated scores
    public static void updateLeaderboard(ArrayList<PlayerScore> leaderboard, AnchorPane paneLeaderboard,
                                         int screenWidth, Scene welcomeScene, Stage stage) {
        Label leaderboardLabel = new Label("Leaderboard");
        Button leaderboardBackBtn = new Button("Back");
        leaderboardBackBtn.setOnAction((ActionEvent event) -> stage.setScene(welcomeScene));
        AnchorPane.setLeftAnchor(leaderboardLabel, (screenWidth / 2.0) - 135);
        AnchorPane.setTopAnchor(leaderboardLabel, 20.0);

        AnchorPane.setRightAnchor(leaderboardBackBtn, 20.0);
        AnchorPane.setBottomAnchor(leaderboardBackBtn, 20.0);
        paneLeaderboard.getChildren().addAll(leaderboardLabel, leaderboardBackBtn);

        int counter = 0;
        for (PlayerScore playerScore : leaderboard) {

            Label username = new Label(playerScore.getName());
            Label score = new Label(Integer.toString(playerScore.getScore()));

            username.getStyleClass().add("leaderboardLabel");
            score.getStyleClass().add("leaderboardLabel");
            AnchorPane.setLeftAnchor(username, (screenWidth / 2.0) - 135);
            AnchorPane.setTopAnchor(username, 100.0 + counter);
            AnchorPane.setRightAnchor(score, (screenWidth / 2.0) - 125);
            AnchorPane.setTopAnchor(score, 100.0 + counter);
            counter += 20;
            paneLeaderboard.getChildren().addAll(username, score);
        }
    }

    // Creates an ArrayList, where the first object has the highest rank and the last object has the lowest.
    public static void createLeaderboard(ArrayList<PlayerScore> leaderboard, Hashtable<Integer, PlayerScore> scoreHash) {
        leaderboard.clear();
        for (int i = 1; i < 11; i++) {
            if(scoreHash.containsKey(i)) {
                scoreHash.get(i).setRank(i);
                leaderboard.add(scoreHash.get(i));
            }
            else {
                break;
            }
        }
    }

    // Returns true if there is a new high score
    public static boolean newHighScore(int score, Hashtable<Integer, PlayerScore> scoreHash) {
        for (int i = 1; i < 11; i++) {
            if (scoreHash.containsKey(i)) {
                if (score > scoreHash.get(i).getScore()) {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        return false;
    }

    // Adds new high score into the hash table
    public static void updateTable(PlayerScore player, Hashtable<Integer, PlayerScore> scoreHash) {
        for (int i = 1; i < 11; i++) {
            if (scoreHash.containsKey(i)) {
                if (player.getScore() > scoreHash.get(i).getScore()) {
                    updateScores(i, scoreHash);
                    scoreHash.put(i, player);
                    break;
                }
            }
            else {
                scoreHash.put(i, player);
                break;
            }
        }
    }

    // Updates rankings inside the hash table when a new score is added.
    public static void updateScores(int start, Hashtable<Integer, PlayerScore> scoreHash) {
        Hashtable<Integer, PlayerScore> temp = new Hashtable<>();
        Enumeration<Integer> e = scoreHash.keys();
        while (e.hasMoreElements()) {
            int key = e.nextElement();
            if (key >= start) {
                temp.put(key + 1, scoreHash.get(key));
            }
        }
        for (int i = 1; i < 11; i++) {
            if (temp.containsKey(i)) {
                scoreHash.put(i, temp.get(i));
            }
        }
    }

    // Saves updated ranks to leaderboard.csv
    public static void saveLeaderboard(ArrayList<PlayerScore> leaderboard) {
        try {
            FileWriter writer = new FileWriter("Snake/src/main/resources/leaderboard.csv");
            for (PlayerScore playerScore : leaderboard) {
                writer.write(playerScore.getName() + "," + playerScore.getScore() + "," + playerScore.getRank() + "\n");
            }
            writer.close();
        }
        catch (Exception e) {
            System.out.println("Couldn't save new score");
        }
    }
}
