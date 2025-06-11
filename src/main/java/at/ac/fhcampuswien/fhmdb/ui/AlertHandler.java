package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.scene.control.Alert;

public class AlertHandler {

    public static void throwAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setContentText(message);
        alert.setHeaderText("Your Exception Message");
        alert.showAndWait();

    }
}
