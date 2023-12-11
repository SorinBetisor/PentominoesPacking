package Phase1.DX;

/**
 * A class representing a cell in the Dancing Links data structure.
 */

public class Cell {
    public int row;
    public Cell U;
    public Cell D;
    public Cell L;
    public Cell R;
    public Header C;

    public int pentID;

     /**
     * Constructs a new cell linked to the specified header.
     *
     * @param header The header to which the cell is linked.
     */
    public Cell(Header header) {
        row = -1;
        L = this;
        R = this;
        U = this;
        D = this;
        C = header;

        pentID=-1;
    }

    /**
     * Inserts a cell to the left of this cell.
     *
     * @param cell The cell to insert to the left of this cell.
     */
    public void InsertLeft(Cell cell) {
        cell.L = L;
        L.R = cell;
        L = cell;
        cell.R = this;
    }

    /**
     * Inserts a cell above this cell.
     *
     * @param cell The cell to insert above this cell.
     */
    public void InsertUp(Cell cell) {
        cell.U = U;
        U.D = cell;
        U = cell;
        cell.D = this;
    }

    /**
     * Covers this cell in the matrix by unlinking it from its left and right neighbors.
     */
    public void coverCell() {
        this.L.R = this.R;
        this.R.L = this.L;
    }

    /**
     * Uncovers this cell by relinking it to its left and right neighbors.
     */
    public void uncoverCell() {
        this.R.L = this;
        this.L.R = this;
    }

     /**
     * Main function for testing the Cell class.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Header head = new Header(-1);
        // Cell a = new Cell(head);
        // Cell b = new Cell(head);
        // Cell c = new Cell(head);
        // Cell d = new Cell(head);

        // head.InsertUp(a);
        // head.InsertUp(b);
        // head.InsertUp(c);
        // head.InsertUp(d);

        // System.out.println("Initial: ");
        // // traverse Left-Right
        // // for(Cell current = root.R; current!=root;current= current.R)
        // // {
        // // System.out.print(current.name);
        // // }

        // // traverse Up-Down
        // for(Cell current = head.D; current!=head; current = current.D)
        // {
        // System.out.println(current.C.name);
        // }
    }

    
    /** 
     * @return Cell
     */
    // getters and setters
    public Cell getU() {
        return U;
    }

    
    /** 
     * @param u
     */
    public void setU(Cell u) {
        U = u;
    }

    public Cell getD() {
        return D;
    }

    public void setD(Cell d) {
        D = d;
    }

    public Cell getL() {
        return L;
    }

    public void setL(Cell l) {
        L = l;
    }

    public Cell getR() {
        return R;
    }

    public void setR(Cell r) {
        R = r;
    }
}
