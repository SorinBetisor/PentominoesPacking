public class Cell {
    public Cell U;
    public Cell D;
    public Cell L;
    public Cell R;
    public Header C;

    public Cell(Header header) {
        L = this;
        R = this;
        U = this;
        D = this;
        C = header;

    }

    public void InsertLeft(Cell cell) {
        cell.L = L;
        L.R = cell;
        L = cell;
        cell.R = this;
    }

    public void InsertUp(Cell cell) {
        cell.U = U;
        U.D = cell;
        U = cell;
        cell.D = this;
    }

    public void coverCell() {
        this.L.R = this.R;
        this.R.L = this.L;
    }

    public void uncoverCell() {
        this.R.L = this;
        this.L.R = this;
    }

    public static void main(String[] args) {
        Header head = new Header("root");
        Cell a = new Cell(head);
        Cell b = new Cell(head);
        Cell c = new Cell(head);
        Cell d = new Cell(head);

        head.InsertUp(a);
        head.InsertUp(b);
        head.InsertUp(c);
        head.InsertUp(d);

        System.out.println("Initial: ");
        // traverse Left-Right
        // for(Cell current = root.R; current!=root;current= current.R)
        // {
        // System.out.print(current.name);
        // }

        // traverse Up-Down
        for(Cell current = head.D; current!=head; current = current.D)
        {
        System.out.println(current.C.name);
        }
    }

    // getters and setters
    public Cell getU() {
        return U;
    }

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
