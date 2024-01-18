package Phase3.Visualizer;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * The RotatableGroup class extends the Group class and provides additional functionality for rotation.
 */
public class RotatableGroup extends Group {

    public Rotate r;
    public Transform t = new Rotate();

    /**
     * Rotates the group around the X-axis by the specified angle.
     * 
     * @param ang the angle of rotation in degrees
     */
    public void rotateByX(int ang) {
        r = new Rotate(ang, Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    /**
     * Rotates the group around the Y-axis by the specified angle.
     * 
     * @param ang the angle of rotation in degrees
     */
    public void rotateByY(int ang) {
        r = new Rotate(ang, Rotate.Y_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    /**
     * Rotates the group around the Z-axis by the specified angle.
     * 
     * @param ang the angle of rotation in degrees
     */
    public void rotateByZ(int ang) {
        r = new Rotate(ang, Rotate.Z_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    /**
     * Sets the pivoting point for rotation.
     * 
     * @param x the x-coordinate of the pivoting point
     * @param y the y-coordinate of the pivoting point
     * @param z the z-coordinate of the pivoting point
     */
    public void setPivotingPoint(double x, double y, double z) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setTranslateZ(z);
    }

    public Rotate rotateXAtt = new Rotate();
    {
        rotateXAtt.setAxis(Rotate.X_AXIS);
    }
    public Rotate rotateYAtt = new Rotate();
    {
        rotateYAtt.setAxis(Rotate.Y_AXIS);
    }
    public Rotate rotateZAtt = new Rotate();
    {
        rotateZAtt.setAxis(Rotate.Z_AXIS);
    }

    /**
     * Sets the pivoting point for rotation using integer coordinates.
     * 
     * @param xCoord the x-coordinate of the pivoting point
     * @param yCoord the y-coordinate of the pivoting point
     * @param zCoord the z-coordinate of the pivoting point
     */
    public void setPivotingPoint(int xCoord, int yCoord, int zCoord) {
        this.rotateXAtt.setPivotX(xCoord);
        this.rotateXAtt.setPivotY(yCoord);
        this.rotateXAtt.setPivotZ(zCoord);
        this.rotateYAtt.setPivotX(xCoord);
        this.rotateYAtt.setPivotY(yCoord);
        this.rotateYAtt.setPivotZ(zCoord);
        this.rotateZAtt.setPivotX(xCoord);
        this.rotateZAtt.setPivotY(yCoord);
        this.rotateZAtt.setPivotZ(zCoord);
    }
}
