package Phase3.Visualizer;

import java.io.IOException;

import Phase3.PiecesDB.ParcelDB;
import Phase3.PiecesDB.PentominoesDB;
import Phase3.Solvers.Greedy;
import Phase3.Solvers.SearchWrapper;
import Phase3.Solvers.DancingLinks.DLX3D;
import Phase3.Solvers.DancingLinks.DancingLinks2;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * The FXVisualizer class is a JavaFX application that visualizes a 3D container
 * and allows users to interact with it.
 * It provides methods for handling user interface events, computing results
 * based on selected options, and updating the UI.
 * The class extends the Application class from JavaFX and overrides the start()
 * method as the entry point of the application.
 */
public class FXVisualizer extends Application {

    private final int SCREEN_WIDTH = 750;
    private final int SCREEN_HEIGHT = 1200;
    private final double ROTATION_SPEED = 0.2;
    private final int BLOCK_SIZE = 10;
    private final double PADDING = 0.5;
    public static final int CARGO_HEIGHT = 8;
    public static final int CARGO_WIDTH = 5;
    public static final int CARGO_DEPTH = 33;
    private final int CARGO_X = (CARGO_WIDTH * BLOCK_SIZE) / 2;
    private final int CARGO_Y = (CARGO_HEIGHT * BLOCK_SIZE) / 2;
    private final int CARGO_Z = (CARGO_DEPTH * BLOCK_SIZE) / 2;
    private final int CARGO_VOLUME = 1320;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(70);

    private RotatableGroup piecesGroup = new RotatableGroup();
    public RotatableGroup rootGroup = new RotatableGroup();
    public Scene visualizerScene;
    public Stage stage;
    public Camera camera;
    private Group worldGroup = new Group();
    private SubScene uiScene;
    private Parent uiRoot;

    public static int[][][] field = new int[CARGO_DEPTH][CARGO_HEIGHT][CARGO_WIDTH];
    public static int quantity1;
    public static int quantity2;
    public static int quantity3;
    public static int valueAL;
    public static int valueBP;
    public static int valueCT;
    public static boolean unlimited = false;

    /**
     * This method is the entry point of the JavaFX application.
     * It initializes the UI components, sets up the scene, and handles user
     * interactions.
     *
     * @param primaryStage the primary stage of the JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {

        camera = new PerspectiveCamera();
        uiRoot = loadUI();

        if (uiRoot == null) {
            System.err.println("Error loading UI. Exiting application.");
            return;
        }

        rootGroup.translateXProperty().set(SCREEN_WIDTH / 2.0 + 100);
        rootGroup.translateYProperty().set(SCREEN_HEIGHT / 4 + 25);
        rootGroup.translateZProperty().set(-500);
        visualizerScene = new Scene(worldGroup, SCREEN_HEIGHT, SCREEN_WIDTH, true, SceneAntialiasing.BALANCED);
        worldGroup.getChildren().addAll(rootGroup);
        visualizerScene.setCamera(camera);
        initializeVisualizer();
        addMouseRotationHandler(visualizerScene, rootGroup, primaryStage, camera);
        addKeyRotationHandlers(visualizerScene, rootGroup, camera, uiRoot);
        primaryStage.setScene(visualizerScene);
        uiScene = new SubScene(uiRoot, SCREEN_HEIGHT, SCREEN_WIDTH, true, SceneAntialiasing.BALANCED);
        primaryStage.setTitle("3D Container Visualizer");
        primaryStage.setResizable(false);
        worldGroup.getChildren().add(uiScene);
        handleUI();
        primaryStage.show();

    }

    /**
     * Handles the user interface by calling the necessary methods to handle button
     * presses,
     * quantities, parcel types, and unlimited DLX.
     */
    public void handleUI() {
        handleComputeButtonPressed();
        handleStopButtonPressed();
        handleQuantities();
        handleParcelTypesText();
        handleUnlimitedDLX();
    }

    /**
     * Clears the field by setting all elements to 0.
     */

    /**
     * Initializes the visualizer by creating cargo container outlines and drawing
     * the container on the field.
     */
    public void initializeVisualizer() {
        createCargoContainerOutlines(rootGroup);
        drawContainer(field, rootGroup);
    }

    public static void main(String args[]) {
        launch(args);
    }

    /**
     * Creates the outlines of a cargo container using the specified dimensions and
     * adds them to the root group.
     * 
     * @param rootGroup the root group to which the outlines will be added
     */
    public void createCargoContainerOutlines(Group rootGroup) {
        int cargoWidth = CARGO_X * 2;
        int cargoHeight = CARGO_Y * 2;
        int cargoDepth = CARGO_Z * 2;
        int padding = -(BLOCK_SIZE / 2);
        Point3D[] points = {
                new Point3D(padding, padding, padding),
                new Point3D(cargoWidth + padding, padding, padding),
                new Point3D(padding, cargoHeight + padding, padding),
                new Point3D(cargoWidth + padding, cargoHeight + padding, padding),
                new Point3D(padding, padding, cargoDepth + padding),
                new Point3D(cargoWidth + padding, padding, cargoDepth + padding),
                new Point3D(padding, cargoHeight + padding, cargoDepth + padding),
                new Point3D(cargoWidth + padding, cargoHeight + padding, cargoDepth + padding)
        };
        int[][] lines = {
                { 0, 1 }, { 0, 2 }, { 0, 4 }, { 1, 5 }, { 1, 3 }, { 2, 3 },
                { 2, 6 }, { 3, 7 }, { 4, 5 }, { 4, 6 }, { 5, 7 }, { 6, 7 }
        };
        for (int[] line : lines) {
            drawLine(points[line[0]], points[line[1]], rootGroup);
        }
    }

    /**
     * Draws the container based on the given field array.
     * Each non-zero value in the field array represents a block to be drawn.
     * The blocks are represented by Box objects and added to the piecesGroup.
     * The piecesGroup is then added to the rootGroup.
     * 
     * @param field     The 3D array representing the field.
     * @param rootGroup The root group to which the piecesGroup will be added.
     */
    private void drawContainer(int[][][] field, RotatableGroup rootGroup) {
        piecesGroup.getChildren().clear();
        for (int z = 0; z < field.length; z++) {
            for (int y = 0; y < field[z].length; y++) {
                for (int x = 0; x < field[z][y].length; x++) {
                    if (field[z][y][x] != 0) {
                        Box box = new Box(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        PhongMaterial material = new PhongMaterial();
                        material.setDiffuseColor(getColor(field[z][y][x]));
                        box.setMaterial(material);
                        box.setTranslateX(x * BLOCK_SIZE + PADDING);
                        box.setTranslateY(y * BLOCK_SIZE + PADDING);
                        box.setTranslateZ(z * BLOCK_SIZE + PADDING);
                        piecesGroup.getChildren().add(box);
                    }
                }
            }
        }
        rootGroup.getChildren().add(piecesGroup);
        piecesGroup.toBack();
    }

    /**
     * Draws a line between the specified origin and target points in 3D space.
     * The line is added to the specified root group.
     *
     * @param origin    the starting point of the line
     * @param target    the ending point of the line
     * @param rootGroup the group to which the line will be added
     */
    private void drawLine(Point3D origin, Point3D target, Group rootGroup) {
        double lineWidth = 1.0;
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D lineVector = target.subtract(origin);
        double lineHeight = lineVector.magnitude();
        Point3D midPoint = target.midpoint(origin);
        Translate moveToOriginCenter = new Translate(midPoint.getX(), midPoint.getY(), midPoint.getZ());
        Point3D rotationAxis = lineVector.crossProduct(yAxis);
        double angle = Math.acos(lineVector.normalize().dotProduct(yAxis));
        Rotate rotateTransform = new Rotate(-Math.toDegrees(angle), rotationAxis);
        Cylinder line = new Cylinder(lineWidth, lineHeight);
        line.setMaterial(new PhongMaterial(Color.BLACK));
        line.getTransforms().addAll(moveToOriginCenter, rotateTransform);
        rootGroup.getChildren().addAll(line);
    }

    // CARGO ROTATION HANDLERS

    /**
     * Adds key rotation handlers to the scene.
     * This method allows the user to rotate the camera using the keyboard keys W,
     * S, A, D, Q, and E.
     * The rotation is applied to the specified rootGroup and camera.
     * 
     * @param scene     the scene to add the key rotation handlers to
     * @param rootGroup the root group to apply the rotation to
     * @param camera    the camera to rotate
     * @param uiRoot    the parent UI root
     */
    public void addKeyRotationHandlers(Scene scene, RotatableGroup rootGroup, Camera camera, Parent uiRoot) {
        Translate cameraTranslate = new Translate();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    cameraTranslate.setY(cameraTranslate.getY() + 10);
                    break;
                case S:
                    cameraTranslate.setY(cameraTranslate.getY() - 10);
                    break;
                case A:
                    cameraTranslate.setZ(cameraTranslate.getZ() + 10);
                    break;
                case D:
                    cameraTranslate.setZ(cameraTranslate.getZ() - 10);
                    break;
                case Q:
                    cameraTranslate.setX(cameraTranslate.getX() + 10);
                    break;
                case E:
                    cameraTranslate.setX(cameraTranslate.getX() - 10);
                    break;
                default:
                    break;
            }

            // Preserve existing rotations
            Rotate xRotate = rootGroup.getTransforms().stream()
                    .filter(transform -> transform instanceof Rotate && ((Rotate) transform).getAxis() == Rotate.X_AXIS)
                    .map(transform -> (Rotate) transform)
                    .findFirst()
                    .orElse(new Rotate(0, Rotate.X_AXIS));

            Rotate yRotate = rootGroup.getTransforms().stream()
                    .filter(transform -> transform instanceof Rotate && ((Rotate) transform).getAxis() == Rotate.Y_AXIS)
                    .map(transform -> (Rotate) transform)
                    .findFirst()
                    .orElse(new Rotate(0, Rotate.Y_AXIS));

            // Apply camera translation and preserve existing rotations
            rootGroup.getTransforms().clear();
            rootGroup.getTransforms().addAll(
                    xRotate,
                    yRotate,
                    new Translate(camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ()),
                    cameraTranslate);
        });
    }

    /**
     * Adds a mouse rotation handler to the specified scene, allowing the user to
     * rotate the 3D visualizer.
     * 
     * @param scene     The scene to attach the mouse rotation handler to.
     * @param rootGroup The root group of the 3D visualizer.
     * @param stage     The stage containing the scene.
     * @param camera    The camera used for the 3D visualizer.
     */
    public void addMouseRotationHandler(Scene scene, RotatableGroup rootGroup, Stage stage, Camera camera) {
        Rotate xRotate;
        Rotate yRotate;
        rootGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS));
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - ROTATION_SPEED * (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + ROTATION_SPEED * (anchorX - event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            rootGroup.translateZProperty().set(rootGroup.getTranslateZ() + delta);
        });
    }

    // HANDLERS FOR UI
    /**
     * Handles the event when the compute button is pressed.
     * Retrieves the selected options from the UI elements and performs the
     * corresponding actions based on the selected algorithm and type of pieces.
     * Updates the UI with the computed results.
     */
    public static long startTime;
    private void handleComputeButtonPressed() {
        // Retrieve the selected options
        Button compute = (Button) uiRoot.lookup("#computeButton");
        @SuppressWarnings("unchecked")
        ComboBox<String> typeOfPiecesComboBox = (ComboBox<String>) uiRoot.lookup("#typeOfPiecesComboBox");
        @SuppressWarnings("unchecked")
        ComboBox<String> algorithmComboBox = (ComboBox<String>) uiRoot.lookup("#algorithmComboBox");
        Text totalValueText = (Text) uiRoot.lookup("#valueText");
        Text fullCoverText = (Text) uiRoot.lookup("#fullCovertext");
        TextField ALparcelValueInput = (TextField) uiRoot.lookup("#ALparcelValueInput");
        TextField BPparcelValueInput = (TextField) uiRoot.lookup("#BPparcelValueInput");
        TextField CTparcelValueInput = (TextField) uiRoot.lookup("#CTparcelValueInput");
        TextField quantityFieldAL = (TextField) uiRoot.lookup("#ALparcelTextInput");
        TextField quantityFieldBP = (TextField) uiRoot.lookup("#BPparcelTextInput");
        TextField quantityFieldCT = (TextField) uiRoot.lookup("#CTparcelTextInput");
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchWrapper.wipeField(field);
                Greedy.currentValue = 0;
                String selectedTypeOfPieces = typeOfPiecesComboBox.getValue();
                String selectedAlgorithm = algorithmComboBox.getValue();
                System.out.println("Selected type of pieces: " + selectedTypeOfPieces);
                System.out.println("Selected algorithm: " + selectedAlgorithm);

                valueAL = Integer.parseInt(ALparcelValueInput.getText());
                valueBP = Integer.parseInt(BPparcelValueInput.getText());
                valueCT = Integer.parseInt(CTparcelValueInput.getText());
                String q1Text = quantityFieldAL.getText();
                String q2Text = quantityFieldBP.getText();
                String q3Text = quantityFieldCT.getText();

                CheckBox ALCheckBox = (CheckBox) uiRoot.lookup("#ALCheckBox");
                CheckBox BPCheckBox = (CheckBox) uiRoot.lookup("#BPCheckBox");
                CheckBox CTCheckBox = (CheckBox) uiRoot.lookup("#CTCheckBox");
                if(q1Text.equals(""))
                {
                    quantity1 = 0;
                }
                else
                {
                    quantity1 = Integer.parseInt(q1Text);
                }
                if(q2Text.equals(""))
                {
                    quantity2 = 0;
                }
                else
                {
                    quantity2 = Integer.parseInt(q2Text);
                }
                if(q3Text.equals(""))
                {
                    quantity3 = 0;
                }
                else
                {
                    quantity3 = Integer.parseInt(q3Text);
                }
                int[] values = new int[] { valueAL, valueBP, valueCT };
                Greedy.values = values;

                if (unlimited == false) {
                    // quantity1 = Integer.parseInt(quantityFieldAL.getText());
                    // quantity2 = Integer.parseInt(quantityFieldBP.getText());
                    // quantity3 = Integer.parseInt(quantityFieldCT.getText());
                    Greedy.quantities = new int[] { quantity1, quantity2, quantity3 };
                    Greedy.unlimited = false;
                } else {
                    Greedy.unlimited = true;
                }

                if (selectedAlgorithm == null || selectedTypeOfPieces == null) {
                    return;
                }

                if (selectedTypeOfPieces.equals("Parcels")) {
                    DLX3D.pent = false;
                    Greedy.parcels = new int[][][][][] { ParcelDB.aRotInt, ParcelDB.bRotInt, ParcelDB.cRotInt };
                } else if (selectedTypeOfPieces.equals("Pentominoes")) {
                    DLX3D.pent = true;
                    Greedy.parcels = new int[][][][][] { PentominoesDB.lPentInt, PentominoesDB.pPentInt,
                            PentominoesDB.tPentInt };
                }
                DancingLinks2.refreshDLX2();

                if (selectedAlgorithm.equals("Greedy")) {
                    Greedy.fillParcels(field);
                    if (Greedy.unlimited == false) {
                        int totalVolume = 0;
                        if (selectedTypeOfPieces.equals("Parcels")) {
                            totalVolume = quantity1 * ParcelDB.aVolume + quantity2 * ParcelDB.bVolume
                                    + quantity3 * ParcelDB.cVolume;
                        } else if (selectedTypeOfPieces.equals("Pentominoes")) {
                            totalVolume = quantity1 * PentominoesDB.lVolume + quantity2 * PentominoesDB.pVolume
                                    + quantity3 * PentominoesDB.tVolume;
                        }

                        if (totalVolume > CARGO_VOLUME) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Total volume exceeds cargo volume! \n Some parcels will be unused.");
                            alert.showAndWait();
                        }
                    }
                    totalValueText.setText("Total value: " + (Greedy.currentValue));
                    fullCoverText.setText("Full cover: " + SearchWrapper.checkFullCover(field));
                } else if (selectedAlgorithm.equals("3D Dancing Links")) {
                    DancingLinks2.c = false;
                    boolean a = ALCheckBox.isSelected();
                    boolean b = BPCheckBox.isSelected();
                    boolean c = CTCheckBox.isSelected();
                    if(a && !b && !c)
                    {
                        showAlert();
                        return;
                    }
                    else if(!a && b && !c)
                    {
                        showAlert();
                        return;
                    }
                    else if(!a && !b && c)
                    {
                        showAlert();
                        return;
                    }
                    else{
                    Thread thread = new Thread(() -> {
                        //start new timer to measure execution time
                        startTime = System.currentTimeMillis();
                        DLX3D dlx3D = new DLX3D(selectedTypeOfPieces, values,a,b,c);
                        dlx3D.createPositions();
                    });
                    thread.start();
                    compute.setDisable(true);}
                }
                rootGroup.getChildren().clear();
                drawContainer(field, rootGroup);
                createCargoContainerOutlines(rootGroup);
            }
        };
        compute.setOnAction(submitHandler);
    }

    /**
     * Handles the event when the stop button is pressed.
     */
    private void handleStopButtonPressed() {
        Button stop = (Button) uiRoot.lookup("#stopButton");
        Button compute = (Button) uiRoot.lookup("#computeButton");
        Text totalValueText = (Text) uiRoot.lookup("#valueText");
        Text fullCoverText = (Text) uiRoot.lookup("#fullCovertext");
        EventHandler<ActionEvent> stopHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (DancingLinks2.c == false) {
                    totalValueText.setText("Total value: " + DancingLinks2.oldBestValue);
                } else {
                    totalValueText.setText("Total value: " + Greedy.currentValue);
                }
                fullCoverText.setText("Full cover: " + SearchWrapper.checkFullCover(field));

                DancingLinks2.c = true;
                compute.setDisable(false);
                rootGroup.getChildren().clear();
                drawContainer(field, rootGroup);
                createCargoContainerOutlines(rootGroup);

            }
        };
        stop.setOnAction(stopHandler);
    }

    public void showAlert()
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please select at least 2 pieces");   
        alert.showAndWait();
    }
    /**
     * Handles the quantities of parcels in the visualizer.
     * If the unlimitedCheckBox is selected, it disables the text fields for parcel
     * quantities.
     * Otherwise, it enables the text fields for parcel quantities.
     */
    private void handleQuantities() {
        CheckBox unlimitedCheckBox = (CheckBox) uiRoot.lookup("#unlimitedCheckBox");
        TextField ALparcelTextInput = (TextField) uiRoot.lookup("#ALparcelTextInput");
        TextField BPparcelTextInput = (TextField) uiRoot.lookup("#BPparcelTextInput");
        TextField CTparcelTextInput = (TextField) uiRoot.lookup("#CTparcelTextInput");
        CheckBox ALCheckBox = (CheckBox) uiRoot.lookup("#ALCheckBox");
        CheckBox BPCheckBox = (CheckBox) uiRoot.lookup("#BPCheckBox");
        CheckBox CTCheckBox = (CheckBox) uiRoot.lookup("#CTCheckBox");

        EventHandler<ActionEvent> comboBoxDisablerHander = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (unlimitedCheckBox.isSelected()) {
                    ALparcelTextInput.setDisable(true);
                    BPparcelTextInput.setDisable(true);
                    CTparcelTextInput.setDisable(true);
                    ALCheckBox.setDisable(false);
                    BPCheckBox.setDisable(false);
                    CTCheckBox.setDisable(false);
                    ALCheckBox.setSelected(true);
                    BPCheckBox.setSelected(true);
                    CTCheckBox.setSelected(true);
                    unlimited = true;
                } else {
                    ALparcelTextInput.setDisable(false);
                    BPparcelTextInput.setDisable(false);
                    CTparcelTextInput.setDisable(false);
                    ALCheckBox.setDisable(true);
                    BPCheckBox.setDisable(true);
                    CTCheckBox.setDisable(true);
                    ALCheckBox.setSelected(false);
                    BPCheckBox.setSelected(false);
                    CTCheckBox.setSelected(false);
                    unlimited = false;
                }
            }
        };
        unlimitedCheckBox.setOnAction(comboBoxDisablerHander);

    }

    /**
     * Handles the logic for updating the parcel types and values based on the
     * selected item in the typeOfPiecesComboBox.
     */
    private void handleParcelTypesText() {
        @SuppressWarnings("unchecked")
        ComboBox<String> typeOfPiecesComboBox = (ComboBox<String>) uiRoot.lookup("#typeOfPiecesComboBox");
        Text parcelTypeText1 = (Text) uiRoot.lookup("#parcelTypeText1");
        Text parcelTypeText2 = (Text) uiRoot.lookup("#parcelTypeText2");
        Text parcelTypeText3 = (Text) uiRoot.lookup("#parcelTypeText3");
        Text parcelValueText1 = (Text) uiRoot.lookup("#parcelValueText1");
        Text parcelValueText2 = (Text) uiRoot.lookup("#parcelValueText2");
        Text parcelValueText3 = (Text) uiRoot.lookup("#parcelValueText3");
        EventHandler<ActionEvent> comboBoxDisablerHander = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (typeOfPiecesComboBox.getValue().equals("Parcels")) {
                    parcelTypeText1.setText("qA");
                    parcelTypeText2.setText("qB");
                    parcelTypeText3.setText("qC");
                    parcelValueText1.setText("vA");
                    parcelValueText2.setText("vB");
                    parcelValueText3.setText("vC");
                } else if (typeOfPiecesComboBox.getValue().equals("Pentominoes")) {
                    parcelTypeText1.setText("qL");
                    parcelTypeText2.setText("qP");
                    parcelTypeText3.setText("qT");
                    parcelValueText1.setText("vL");
                    parcelValueText2.setText("vP");
                    parcelValueText3.setText("vT");
                }
            }
        };
        typeOfPiecesComboBox.setOnAction(comboBoxDisablerHander);
    }

    /**
     * Handles the functionality for enabling/disabling unlimited DLX mode.
     * If the selected algorithm is "3D Dancing Links", it disables the input fields
     * and sets the unlimited checkbox to true.
     * Otherwise, it enables the input fields and the unlimited checkbox.
     */
    private void handleUnlimitedDLX() {
        @SuppressWarnings("unchecked")
        ComboBox<String> algorithmComboBox = (ComboBox<String>) uiRoot.lookup("#algorithmComboBox");
        @SuppressWarnings("unchecked")
        ComboBox<String> typeOfPiecesComboBox = (ComboBox<String>) uiRoot.lookup("#typeOfPiecesComboBox");
        TextField ALparcelTextInput = (TextField) uiRoot.lookup("#ALparcelTextInput");
        TextField BPparcelTextInput = (TextField) uiRoot.lookup("#BPparcelTextInput");
        TextField CTparcelTextInput = (TextField) uiRoot.lookup("#CTparcelTextInput");
        CheckBox unlimitedCheckBox = (CheckBox) uiRoot.lookup("#unlimitedCheckBox");
        CheckBox ALCheckBox = (CheckBox) uiRoot.lookup("#ALCheckBox");
        CheckBox BPCheckBox = (CheckBox) uiRoot.lookup("#BPCheckBox");
        CheckBox CTCheckBox = (CheckBox) uiRoot.lookup("#CTCheckBox");
        EventHandler<ActionEvent> comboBoxDisablerHander = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (algorithmComboBox.getValue().equals("3D Dancing Links")) {
                    ALparcelTextInput.setDisable(true);
                    BPparcelTextInput.setDisable(true);
                    CTparcelTextInput.setDisable(true);
                    unlimitedCheckBox.setSelected(true);
                    unlimitedCheckBox.setDisable(true);
                    ALCheckBox.setDisable(false);
                    BPCheckBox.setDisable(false);
                    CTCheckBox.setDisable(false);
                    ALCheckBox.setSelected(true);
                    BPCheckBox.setSelected(true);
                    CTCheckBox.setSelected(true);
                    typeOfPiecesComboBox.getItems().remove("Parcels");
                    unlimited = true;
                } else {
                    unlimitedCheckBox.setDisable(false);
                    unlimitedCheckBox.setSelected(false);
                    ALparcelTextInput.setDisable(false);
                    BPparcelTextInput.setDisable(false);
                    CTparcelTextInput.setDisable(false);
                    ALCheckBox.setDisable(true);
                    BPCheckBox.setDisable(true);
                    CTCheckBox.setDisable(true);
                    ALCheckBox.setSelected(false);
                    BPCheckBox.setSelected(false);
                    CTCheckBox.setSelected(false);
                    typeOfPiecesComboBox.getItems().add("Parcels");
                    Greedy.unlimited = false;
                    unlimited = false;
                }
            }
        };
        algorithmComboBox.setOnAction(comboBoxDisablerHander);
    }

    /**
     * Loads the UI from the FXML file and returns the root pane.
     *
     * @return The root pane of the loaded UI.
     */
    private Pane loadUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
            Pane uiRoot = loader.load();
            return uiRoot;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the color based on the given index.
     *
     * @param i the index of the color
     * @return the color corresponding to the index, or null if the index is invalid
     */
    private Color getColor(int i) {
        if (i == 1) {
            return Color.RED;
        }
        if (i == 2) {
            return Color.BLUE;
        }
        if (i == 3) {
            return Color.GREEN.darker();
        }
        return null;
    }

}
