//Wenting Yu wy2
package hw3;


import javafx.scene.control.Alert;

public class DataException extends RuntimeException{

    /**
     * format exception outputs
     * @param message
     */
    DataException(String message){
        //maybe add super(message) here

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Data Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);

        alert.showAndWait();
    }

}
