package Phase3.Solvers.DancingLinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import Phase3.Solvers.SearchWrapper;
import Phase3.Visualizer.FXVisualizer;

/**
 * A class that implements the Dancing Links algorithm to solve the pentomino
 * puzzle.
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


    public static void refreshDLX2()
    {
        field = new int[depth][height][width];
        c = false;
    }

    /**
     * Constructs a DancingLinks object with the specified number of columns.
     *
     * @param columns The number of columns in the Dancing Links matrix.
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
     * Algorithm X implementation for solving the exact cover problem using Dancing
     * Links.
     * This method recursively searches for solutions to the exact cover problem
     * represented
     * by the given data structure. It utilizes the Dancing Links technique to
     * efficiently
     * explore possible solutions.
     *
     * @param step The current step of the algorithm, which tracks the progress of
     *             the search.
     *             It is used to identify and display progress in finding solutions.
     *
     *             The method operates as follows:
     *             1. If the root node's right neighbor is itself, it means that the
     *             entire matrix has
     *             been covered, indicating a potential solution. In this case, the
     *             method checks for
     *             duplicate solutions and displays a unique one if found.
     *             2. If duplicates are not found, it visualizes and displays the
     *             solution on the user
     *             interface using the provided `ui` object.
     *             3. Otherwise, the method proceeds with the search.
     *             4. It selects the column (constraint) with the fewest
     *             possibilities (heuristic for
     *             efficiency) and covers it, effectively reducing the search space.
     *             5. For each row in the selected column, the method recursively
     *             explores possible
     *             solutions by covering other columns and rows affected by this
     *             choice.
     *             6. If a valid solution is found, it is stored in the `answer`
     *             stack, and the search
     *             continues for the next step.
     *             7. If the solution is not valid, the choices are unmade
     *             (uncovered), and the search
     *             backtracks to explore other possibilities.
     *
     *             The method uses the Dancing Links data structure and efficient
     *             techniques to manage the
     *             exact cover problem, making it an optimal choice for solving
     *             problems like pentominoes.
     */


    public void algorithmX(int step) {
        if(c) return;
        if (root.R == root) {
            System.out.println("Solution found!");
        }
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
                // System.out.println(Arrays.toString(coords));
                SearchWrapper.addPiece(field, row.shape, coords);
                DLX3D.pieceCount++;
                DLX3D.totalValue += row.pieceValue;
            }
            // if(DLX3D.totalValue < 1000){return;}
            FXVisualizer.field = field;
            System.out.println("Solution found!");
            System.out.println("Total value: " + DLX3D.totalValue);
            System.out.println("Piece count: " + DLX3D.pieceCount);
            if(DLX3D.totalValue > 255){ //TODO: change to dynamic
                c = true;
                return;
            }
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
