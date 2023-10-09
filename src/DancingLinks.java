public class DancingLinks {
    public Header root;
    public Header[] headers;

    public DancingLinks(int columns) {
        root = new Header(-1);
        headers = new Header[columns];
        for (int j = 0; j < columns; j++) {
            headers[j] = new Header(j);
            root.InsertLeft(headers[j]);
        }
    }

    // accepts indexes of one only in increasing order!
    public void AddRow(int[] ones) {
        int last = -1;
        Cell first = null;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]);
            headers[x].InsertUp(cell);

            headers[x].size++;

            if (x <= last) {
                throw new IllegalArgumentException("Column indexes must be in increasing order");
            }

            if (first == null) {
                first = cell;
            } else {
                first.InsertLeft(cell);
            }
        }
    }

    public void algorithmX(int step) {
        if (root.R == root) {
            System.out.println("Solution found.");
            return;
        }

        Header head = (Header) root.R;
        cover(head);

        for (Cell rCell = head.D; rCell != head; rCell = rCell.D) 
        {
            // save row number
            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R)
                cover(jCell.C);

            algorithmX(step + 1);

            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L)
                uncover(jCell.C);
        }
        uncover(head);
    }

    private void cover(Header head) {

    }

    private void uncover(Header head) {

    }

    public void test() {
        DancingLinks dance = new DancingLinks(45);

        dance.AddRow(new int[] { 24, 33, 34 });
        dance.AddRow(new int[] { 34, 43, 44 });
        dance.AddRow(new int[] { 33, 42, 43 });
        dance.AddRow(new int[] { 13, 14, 24 });

        dance.AddRow(new int[] { 32, 33, 43 });
        dance.AddRow(new int[] { 31, 32, 41 });
        dance.AddRow(new int[] { 23, 32, 33 });
        dance.AddRow(new int[] { 23, 33, 34 });

        dance.AddRow(new int[] { 13, 14, 23 });
        dance.AddRow(new int[] { 31, 41, 42 });
        dance.AddRow(new int[] { 33, 34, 44 });
        dance.AddRow(new int[] { 33, 43, 44 });

        algorithmX(0);
    }

    public static void main(String[] args) {
        DancingLinks dance = new DancingLinks(45);
        dance.test();
    }
}
