package Phase3.Solvers.DancingLinks;
/**
 * A class representing a row in the Dancing Links matrix for the DX algorithm.
 */
public class Row {
    int id;
    int pentID;
    int[][][] shape;
    int x0;
    int y0;
    int z0;
    int pieceValue;

    /**
     * Constructs a new row with the specified attributes.
     *
     * @param id       The identifier of the row.
     * @param x        The x-coordinate of the placement.
     * @param y        The y-coordinate of the placement.
     * @param pentID   The identifier of the pentomino.
     * @param rotation The rotation/mutation of the pentomino.
     */
    public Row(int id, int x, int y, int z0, int pentID, int[][][] shape, int pieceValue) {
        this.id = id;
        this.x0 = x;
        this.y0 = y;
        this.z0 = z0;
        this.pentID = pentID;
        this.shape = shape;
        this.pieceValue = pieceValue;
    }
}