package Phase1.DX;

/**
 * A class representing a row in the Dancing Links matrix for the DX algorithm.
 */
public class Row {
    int id;
    int pentID;
    int mutation;
    int x0;
    int y0;

    /**
     * Constructs a new row with the specified attributes.
     *
     * @param id       The identifier of the row.
     * @param x        The x-coordinate of the placement.
     * @param y        The y-coordinate of the placement.
     * @param pentID   The identifier of the pentomino.
     * @param rotation The rotation/mutation of the pentomino.
     */
    public Row(int id, int x, int y, int pentID, int rotation) {
        this.id = id;
        this.x0 = x;
        this.y0 = y;
        this.pentID = pentID;
        this.mutation = rotation;
    }
}