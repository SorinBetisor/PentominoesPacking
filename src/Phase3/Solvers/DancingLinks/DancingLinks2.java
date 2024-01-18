package Phase3.Solvers.DancingLinks;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import Phase3.Solvers.SearchWrapper;
import Phase3.Visualizer.FXVisualizer;

/**
 * The DancingLinks2 class represents a solver using the Dancing Links technique
 * to efficiently search for solutions to the 3D exact cover problem.
 */
public class DancingLinks2 {

    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;
    public Stack<Integer> pentIDS;
    public int duplicateSolutionsFound;
    public static int depth = FXVisualizer.CARGO_DEPTH;
    public static int height = FXVisualizer.CARGO_HEIGHT;
    public static int width = FXVisualizer.CARGO_WIDTH;
    public static int[][][] field = new int[depth][height][width];
    public static boolean c = false;
    public static int limitDL2;
    public static int oldBestValue = 0;

    /**
     * Constructs a DancingLinks2 object with the specified number of columns.
     *
     * @param columns the number of columns in the DancingLinks2 object
     */
    public DancingLinks2(int columns) {
        answer = new Stack<Integer>();
        pentIDS = new Stack<Integer>();

        duplicateSolutionsFound = 0;

        root = new Header(-1);
        headers = new Header[columns];
        for (int j = 0; j < columns; j++) {
            headers[j] = new Header(j);
            root.InsertLeft(headers[j]);
        }
    }

    /**
     * Refreshes the Dancing Links solver by resetting the old best value, 
     * creating a new field, and setting the 'c' flag to false.
     */
    public static void refreshDLX2()
    {
        oldBestValue = 0;
        field = new int[depth][height][width];
        c = false;
    }

    /**
     * Adds a row to the Dancing Links matrix.
     * 
     * @param row     The row index.
     * @param pentId  The ID of the pentomino.
     * @param ones    An array of column indexes with value 1.
     * @param piece   The shape of the pentomino.
     * @throws IllegalArgumentException if the column indexes are not in increasing order.
     */
    public void AddRow(int row, int pentId, int[] ones, int[][][] piece) {
        int last = -1;
        Cell first = null;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]);
            headers[x].InsertUp(cell);
            cell.row = row;
            cell.shape = piece;
            cell.pentID = pentId;

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

    /**
     * Executes the Algorithm X for solving the exact cover problem.
     * This method uses the Dancing Links technique to efficiently search for solutions.
     *
     * @param step The current step in the algorithm.
     */
    public void algorithmX(int step) {
        if(c) return;
        List<Row> rows = new ArrayList<Row>();
        if(answer.size() > 1){
            DLX3D.pieceCount = 0;
            DLX3D.totalValue = 0;
            for(var row : answer)
            {
                Row r = DLX3D.rows.get(row);
                rows.add(r);

            }

            for(var row : rows)
            {
                int[] coords = new int[]{row.z0,row.y0,row.x0};
                SearchWrapper.addPiece(field, row.shape, coords);
                DLX3D.pieceCount++;
                DLX3D.totalValue += row.pieceValue;
            }
            if(DLX3D.totalValue > oldBestValue){
            oldBestValue = DLX3D.totalValue;
            FXVisualizer.field = field;
            // System.out.println("New best value: " + oldBestValue);
            }
            
            if(c) return;
        }
        {
            
        }

        Header head = (Header) root.R;
        int minSize = head.size;
        for (Cell xCell = head; xCell != root; xCell = xCell.R) // optimising branching factor
        {
            if (((Header) xCell).size < minSize) {
                minSize = ((Header) xCell).size;
                head = (Header) xCell;

                if (head.C.size == 0) {
                    return;
                }
            }
        }
        cover(head);
        for (Cell rCell = head.D; rCell != head; rCell = rCell.D) {
            answer.push(rCell.row);
            pentIDS.push(rCell.pentID);

            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R) {
                cover(jCell.C);
            }
            algorithmX(step + 1);
            answer.pop();
            pentIDS.pop();

            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L) {
                uncover(jCell.C);
            }

        }
        uncover(head);
    }

    /**
     * Cover a column in the Dancing Links matrix along with all the rows and cells
     * it covers.
     *
     * @param head The header cell representing the column to be covered.
     */
    private void cover(Header head) {
        head.R.L = head.L;
        head.L.R = head.R;

        for (Cell iCell = head.D; iCell != head; iCell = iCell.D) {
            for (Cell jCell = iCell.R; iCell != jCell; jCell = jCell.R) {
                jCell.D.U = jCell.U;
                jCell.U.D = jCell.D;
                jCell.C.size--;
            }
        }
    }

    /**
     * Uncover a column in the Dancing Links matrix along with all the rows and
     * cells it covers.
     *
     * @param head The header cell representing the column to be uncovered.
     */
    private void uncover(Header head) {
        for (Cell iCell = head.U; iCell != head; iCell = iCell.U)
            for (Cell jCell = iCell.L; jCell != iCell; jCell = jCell.L) {
                jCell.D.U = jCell;
                jCell.U.D = jCell;
                jCell.C.size++;
            }
        head.R.L = head;
        head.L.R = head;
    }

}
