package Phase1.DX;
public class Row {
    int id;
    int pentID;
    int mutation;
    int x0;
    int y0;

    public Row(int id, int x, int y, int pentID, int rotation) {
        this.id = id;
        this.x0 = x;
        this.y0 = y;
        this.pentID = pentID;
        this.mutation = rotation;
    }
}