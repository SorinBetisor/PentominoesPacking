package Phase3.Visualizer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private ChoiceBox <String> pieces;
    @FXML
    private ChoiceBox<String> algorithm;

    private String[] piecesList = {"Parcels", "Pentomino"};
    private String[] algorithms = {"Dancing Links"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pieces.getItems().addAll(piecesList);
        algorithm.getItems().addAll(algorithms);
    }
}
