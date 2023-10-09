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
    //accepts indexes of one only in increasing order!
    public void AddRow(int[] ones) {
        Cell first = null;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]);
            headers[x].InsertUp(cell);

            headers[x].size++;

            if (first == null) {
                first = cell;
            } else {
                first.InsertLeft(cell);
            }
        }
    }
}
