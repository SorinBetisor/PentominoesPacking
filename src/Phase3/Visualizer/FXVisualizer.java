package Phase3.Visualizer;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
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
    public static int CARGO_HEIGHT = 8;
    public static int CARGO_WIDTH = 5;
    public static int CARGO_DEPTH = 33;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) {
        Box box = new Box(100, 20, 50);
        box.setMaterial(new PhongMaterial(Color.RED));

        RotatableGroup root = new RotatableGroup();
        root.translateXProperty().set(SCREEN_WIDTH / 2.0 + 225);
        root.translateYProperty().set(SCREEN_HEIGHT / 3); // Adjust this value as needed
        root.translateZProperty().set(500);

        // root.getChildren().add(box);
        createCargoContainerOutlines(root);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(root, SCREEN_HEIGHT, SCREEN_WIDTH, true);
        scene.setCamera(camera);

        stage.setTitle("Rotating Cube");

        // addKeyRotationHandlers(scene, root);
        addMouseRotationHandler(scene, root);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }

    public void createCargoContainerOutlines(Group root)
    {
        int spacing = -(BLOCK_SIZE*2);
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(CARGO_WIDTH*spacing, 0, 0);
        Point3D p3 = new Point3D(0, CARGO_HEIGHT*spacing, 0);
        Point3D p4 = new Point3D(CARGO_WIDTH*spacing, CARGO_HEIGHT*spacing, 0);
        Point3D p5 = new Point3D(0, 0, CARGO_DEPTH*spacing);
        Point3D p6 = new Point3D(CARGO_WIDTH*spacing, 0, CARGO_DEPTH*spacing);
        Point3D p7 = new Point3D(0, CARGO_HEIGHT*spacing, CARGO_DEPTH*spacing);
        Point3D p8 = new Point3D(CARGO_WIDTH*spacing, CARGO_HEIGHT*spacing, CARGO_DEPTH*spacing);


        drawLine(p1, p2,root);
        drawLine(p1, p3,root);
        drawLine(p1, p5,root);
        drawLine(p2, p6,root);
        drawLine(p2, p4,root);
        drawLine(p3, p4,root);
        drawLine(p3, p7,root);
        drawLine(p4, p8,root);
        drawLine(p5, p6,root);
        drawLine(p5, p7,root);
        drawLine(p6, p8,root);
        drawLine(p7, p8,root);
    }

    public void drawLine(Point3D origin, Point3D target, Group root) {
        double lineWidth = 1.4;
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

    // 

    public void addMouseRotationHandler(Scene scene, RotatableGroup root) {
        Rotate xRotate;
        Rotate yRotate;
        root.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - ROTATION_SPEED*(anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + ROTATION_SPEED*(anchorX - event.getSceneX()));
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


    // public void addKeyRotationHandlers(Scene scene, RotatableGroup root) {
    //     scene.setOnKeyPressed(event -> {
    //         switch (event.getCode()) {
    //             case W:
    //                 root.rotateByX(-ROTATION_SPEED);
    //                 break;
    //             case S:
    //                 root.rotateByX(ROTATION_SPEED);
    //                 break;
    //             case A:
    //                 root.rotateByY(-ROTATION_SPEED);
    //                 break;
    //             case D:
    //                 root.rotateByY(ROTATION_SPEED);
    //                 break;
    //             case Q:
    //                 root.rotateByZ(-ROTATION_SPEED);
    //                 break;
    //             case E:
    //                 root.rotateByZ(ROTATION_SPEED);
    //                 break;
    //             case UP:
    //                 root.translateZProperty().set(root.getTranslateZ() + 10);
    //                 break;
    //             case DOWN: 
    //                 root.translateZProperty().set(root.getTranslateZ() - 10);
    //                 break;
    //             default:
    //                 break;
    //         }
    //     });
    // }
}
