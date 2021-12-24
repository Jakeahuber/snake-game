package com.example.snake;

import javafx.scene.control.Label;

public class CustomException extends Exception {
    public CustomException() {}

    public CustomException(String message, Label labelInvalidInput) {
        super(message);
        labelInvalidInput.setText(message);
    }
}
