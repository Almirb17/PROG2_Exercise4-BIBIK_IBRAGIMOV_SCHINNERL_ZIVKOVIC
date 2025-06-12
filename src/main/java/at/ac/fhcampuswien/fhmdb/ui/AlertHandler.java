package at.ac.fhcampuswien.fhmdb.ui;

import javafx.scene.control.Alert;

public class AlertHandler {

    public static void throwErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setContentText(message);
        alert.setHeaderText("Your Exception Message");
        alert.showAndWait();

    }

    public static void throwInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
