package Phase3.Visualizer;

import Phase3.Solvers.Greedy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private Button computeButton;

    @FXML
    private ComboBox<String> typeOfPiecesComboBox;

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Text typeOfPiecesText;

    @FXML
    private Text algorithmText;

    @FXML
    private void initialize() {
        typeOfPiecesComboBox.getItems().addAll("Parcels", "Pentominoes");
        algorithmComboBox.getItems().addAll("Greedy","3D Dancing Links");
    }

     @FXML
    private void onComputeButtonPressed(ActionEvent event) {
        // Retrieve the selected options
        String selectedTypeOfPieces = typeOfPiecesComboBox.getValue();
        String selectedAlgorithm = algorithmComboBox.getValue();
        // System.out.println("Selected type of pieces: " + selectedTypeOfPieces);
        // System.out.println("Selected algorithm: " + selectedAlgorithm);
        // FXVisualizer.selectedTypeOfPieces = selectedTypeOfPieces;
        // FXVisualizer.selectedAlgorithm = selectedAlgorithm;
    }

}
