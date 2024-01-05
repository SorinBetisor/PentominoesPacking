package Phase3.Visualizer;
import Phase3.Solvers.Greedy;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

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
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private RotatableGroup piecesGroup = new RotatableGroup();
    public RotatableGroup root = new RotatableGroup();

    int[][][] field = new int[CARGO_DEPTH][CARGO_HEIGHT][CARGO_WIDTH];

    @Override
    public void start(Stage stage) {
        root.translateXProperty().set(SCREEN_WIDTH / 2.0 + 225);
        root.translateYProperty().set(SCREEN_HEIGHT / 4);
        root.translateZProperty().set(-500);
        createCargoContainerOutlines(root);
        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(root, SCREEN_HEIGHT, SCREEN_WIDTH, true);
        scene.setCamera(camera);
        stage.setTitle("Cargo Visualizer");
        addMouseRotationHandler(scene, root, stage);
        Greedy.fillParcels(field);
        drawContainer(field, root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }

    public void createCargoContainerOutlines(Group root) {
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
            drawLine(points[line[0]], points[line[1]], root);
        }
    }

    private void drawContainer(int[][][] field, RotatableGroup root) {
        piecesGroup.getChildren().clear();
        for (int z = 0; z < field.length; z++) {
            for (int y = 0; y < field[z].length; y++) {
                for (int x = 0; x < field[z][y].length; x++) {
                    if(field[z][y][x] != 0){
                        Box box = new Box(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        PhongMaterial material = new PhongMaterial();
                        material.setDiffuseColor(getColor(field[z][y][x]));
                        box.setMaterial(material);
                        box.setTranslateX(x * BLOCK_SIZE + PADDING);
                        box.setTranslateY(y * BLOCK_SIZE + PADDING);
                        box.setTranslateZ(z * BLOCK_SIZE + PADDING);
                        piecesGroup.getChildren().add(box);}
                }
            }
        }
        root.getChildren().add(piecesGroup);
    }

    private void drawLine(Point3D origin, Point3D target, Group root) {
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
        root.getChildren().addAll(line);
    }

    public void addMouseRotationHandler(Scene scene, RotatableGroup root, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        root.getTransforms().addAll(
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
            root.translateZProperty().set(root.getTranslateZ() + delta);
        });
    }

    class RotatableGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByZ(int ang) {
            r = new Rotate(ang, Rotate.Z_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }

    private Color getColor(int i)
    {
        if(i==1){return Color.RED;}
        if(i==2){return Color.BLUE;}
        if(i==3){return Color.GREEN;}
        return null;
    }

    public void setState(int[][][] field) {
        root.getChildren().clear();
        drawContainer(field, piecesGroup);
    }

    // public void addKeyRotationHandlers(Scene scene, RotatableGroup root) {
    // scene.setOnKeyPressed(event -> {
    // switch (event.getCode()) {
    // case W:
    // root.rotateByX(-ROTATION_SPEED);
    // break;
    // case S:
    // root.rotateByX(ROTATION_SPEED);
    // break;
    // case A:
    // root.rotateByY(-ROTATION_SPEED);
    // break;
    // case D:
    // root.rotateByY(ROTATION_SPEED);
    // break;
    // case Q:
    // root.rotateByZ(-ROTATION_SPEED);
    // break;
    // case E:
    // root.rotateByZ(ROTATION_SPEED);
    // break;
    // case UP:
    // root.translateZProperty().set(root.getTranslateZ() + 10);
    // break;
    // case DOWN:
    // root.translateZProperty().set(root.getTranslateZ() - 10);
    // break;
    // default:
    // break;
    // }
    // });
    // }
}
