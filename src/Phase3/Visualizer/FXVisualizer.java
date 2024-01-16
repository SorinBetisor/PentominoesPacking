package Phase3.Visualizer;

import java.io.IOException;

import Phase3.PiecesDB.ParcelDB;
import Phase3.PiecesDB.PentominoesDB;
import Phase3.Solvers.Greedy;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    public static int[][][] field = new int[CARGO_DEPTH][CARGO_HEIGHT][CARGO_WIDTH];
    Parent uiRoot;


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
        
        handleComputeButtonPressed();
        primaryStage.show();

    }

    public void clearCargoVisualization()
    {
        worldGroup.getChildren().remove(rootGroup);
    }

    public void wipeField()
    {
        for(int z=0;z<field.length;z++)
        {
            for(int y=0;y<field[z].length;y++)
            {
                for(int x=0;x<field[z][y].length;x++)
                {
                    field[z][y][x] = 0;
                }
            }
        }
    }

    public void handleComputeButtonPressed() {
        // Retrieve the selected options
        Button compute = (Button) uiRoot.lookup("#computeButton");
        ComboBox<String> typeOfPiecesComboBox = (ComboBox<String>) uiRoot.lookup("#typeOfPiecesComboBox");
        ComboBox<String> algorithmComboBox = (ComboBox<String>) uiRoot.lookup("#algorithmComboBox");
        Text totalValueText = (Text) uiRoot.lookup("#valueText");
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wipeField();
                Greedy.currentValue = 0;
                String selectedTypeOfPieces = typeOfPiecesComboBox.getValue();
                String selectedAlgorithm = algorithmComboBox.getValue();
                System.out.println("Selected type of pieces: " + selectedTypeOfPieces);
                System.out.println("Selected algorithm: " + selectedAlgorithm);
                
                if(selectedAlgorithm == null || selectedTypeOfPieces == null)
                {
                    return;
                }

                if(selectedTypeOfPieces.equals("Parcels"))
                {
                    DLX3D.pent = false;
                    DancingLinks2.limitDL2 = 255;
                    Greedy.parcels = new int[][][][][]{ParcelDB.aRotInt,ParcelDB.bRotInt,ParcelDB.cRotInt};
                }
                else if(selectedTypeOfPieces.equals("Pentominoes"))
                {
                    DLX3D.pent = true;
                    DancingLinks2.limitDL2 = 1178;
                    Greedy.parcels = new int[][][][][]{PentominoesDB.lPentInt,PentominoesDB.pPentInt,PentominoesDB.tPentInt};
                }
                DancingLinks2.refreshDLX2();
                DLX3D dlx3D = new DLX3D(selectedTypeOfPieces);

                if (selectedAlgorithm.equals("Greedy")) {
                    Greedy.fillParcels(field);
                    totalValueText.setText("Total value: " + Greedy.currentValue);
                } else if (selectedAlgorithm.equals("3D Dancing Links")) {                    
                    dlx3D.createPositions();
                    totalValueText.setText("Total value: "+DLX3D.totalValue);
                }
                rootGroup.getChildren().clear();
                drawContainer(field, rootGroup);
                createCargoContainerOutlines(rootGroup);
            }
        };
        compute.setOnAction(submitHandler);

    }


    public void initializeVisualizer() {
        createCargoContainerOutlines(rootGroup);
        // Greedy.fillParcels(field);
        // DLX3D dlx3D = new DLX3D();
        // dlx3D.createPositions();
        // Greedy.fillParcels(field);
        // System.out.println("Fully covered: " + SearchWrapper.checkFullCover(field));
        drawContainer(field, rootGroup);
    }
    public static void main(String args[]) {
        launch(args);
    }

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

    public void setState(int[][][] field) {
        rootGroup.getChildren().clear();
        drawContainer(field, piecesGroup);
    }

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


    //ROBIN: this loads the UI from FXML
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
    

}
