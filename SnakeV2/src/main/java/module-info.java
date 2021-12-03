module com.example.snakev2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.snakev2 to javafx.fxml;
    exports com.example.snakev2;
}