package Phase3.Visualizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * The Controller class is responsible for controlling the user interface and handling user interactions.
 * It contains methods and fields annotated with @FXML that are used to bind UI elements to the controller.
 */
public class Controller {
    @FXML
    private TextField ALparcelTextInput;

    @FXML
    private TextField BPparcelTextInput;

    @FXML
    private TextField CTparcelTextInput;

    @FXML
    private Text parcelTypeText1;

    @FXML
    private Text parcelTypeText2;

    @FXML
    private Text parcelTypeText3;

    @FXML
    private Text fullCovertext;

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
    private CheckBox unlimitedCheckBox;

    @FXML
    Text parcelValueText1;

    @FXML
    Text parcelValueText2;

    @FXML
    Text parcelValueText3;

    @FXML
    private TextField ALparcelValueInput;

    @FXML
    private TextField BPparcelValueInput;

    @FXML
    private TextField CTparcelValueInput;

    @FXML
    private Button stopButton;

    /**
     * Initializes the controller.
     * Adds items to the typeOfPiecesComboBox and algorithmComboBox.
     */
    @FXML
    private void initialize() {
        typeOfPiecesComboBox.getItems().addAll("Parcels", "Pentominoes");
        algorithmComboBox.getItems().addAll("Greedy","3D Dancing Links");
    }

    @FXML
    private void onComputeButtonPressed(ActionEvent event) {
    }

}
