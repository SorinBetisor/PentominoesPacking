package Phase1.DX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import Phase1.Search;
import Phase1.UI;

/**
 * A class that implements the Dancing Links algorithm to solve the pentomino
 * puzzle.
 */
public class DancingLinks {

    public static final char[] INPUT = Search.INPUT;
    private static final UI ui = Search.ui;

    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;
    public Stack<Integer> pentIDS;
    public List<Row> rows;
    public int duplicateSolutionsFound;

    /**
     * Constructs a DancingLinks object with the specified number of columns.
     *
     * @param columns The number of columns in the Dancing Links matrix.
     */
    public DancingLinks(int columns) {
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
     * Adds a row to the Dancing Links matrix to represent the placement of a
     * pentomino on
     * the board.
     *
     * @param row      The row number to be added to the matrix.
     * @param pentId   The identifier of the pentomino to be placed in this row.
     * @param x0       The X-coordinate on the board where the pentomino is placed.
     * @param y0       The Y-coordinate on the board where the pentomino is placed.
     * @param mutation The specific mutation or rotation of the pentomino to be
     *                 placed.
     * @param ones     An array of column indexes where the pentomino is covering,
     *                 indicating
     *                 the cells that are occupied by the pentomino.
     *
     *                 This method adds a row to the matrix, linking cells to column
     *                 headers, and incrementing
     *                 the size of the headers to track the number of elements
     *                 covering each constraint.
     */
    // accepts indexes of one only in increasing order!
    public void AddRow(int row, int pentId, int x0, int y0, int mutation, int[] ones) {
        int last = -1;
        Cell first = null;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]);
            headers[x].InsertUp(cell);
            cell.row = row;
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
        if (root.R == root) {
            duplicateSolutionsFound++;

            boolean duplicates = false;
            List<Row> rows = new ArrayList<Row>();
            List<Integer> pentIDSList = new ArrayList<Integer>();

            if (duplicateSolutionsFound % 10000 == 0) 
            {
                System.out.println("Found " + duplicateSolutionsFound + " solutions that use duplicate pieces");
            }

            for (var row : answer) {
                Row r = DXSearch.rows.get(row);
                rows.add(r);
                pentIDSList.add(r.pentID);
            }

            duplicates = hasDuplicatesList(pentIDS);
            if (!duplicates) {
                DXSearch.start = System.currentTimeMillis();
                for (var row : rows) {
                    DXSearch.drawPentominoe(row.pentID, row.mutation, row.x0, row.y0);
                }
                ui.setState(Search.field);
                System.out.println("FOUND SOLUTION THAT USES UNIQUE PIECES");
                try {
                    Thread.sleep(1000);
                } catch (Exception ie) {
                }

                double end = System.currentTimeMillis();
		        System.out.println("(DX) Execution time (ms): " + (end - DXSearch.start) / 1000);
                return;
            }
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
            deleteRowsByPentID(rCell.pentID);
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
     * Delete rows in the Dancing Links matrix that correspond to a specific
     * pentomino ID.
     *
     * @param pentID The pentomino ID for which rows are to be deleted.
     */
    public void deleteRowsByPentID(int pentID) {
        List<Cell> cellsToDelete = new ArrayList<>();

        for (Cell iCell = root.D; iCell != root; iCell = iCell.D) {
            if (iCell.pentID == pentID) {
                // Collect all the cells in the row for deletion
                for (Cell jCell = iCell.R; jCell != iCell; jCell = jCell.R) {
                    cellsToDelete.add(jCell);
                }
            }
        }

        // Delete the collected cells
        for (Cell cellToDelete : cellsToDelete) {
            deleteCell(cellToDelete);
        }
    }

    /**
     * Delete a cell in the Dancing Links matrix, which involves unlinking it from
     * its column and row.
     *
     * @param cell The cell to be deleted from the matrix.
     */
    private void deleteCell(Cell cell) {
        // Unlink the cell from its column
        cell.L.R = cell.R;
        cell.R.L = cell.L;
        cell.C.size--;

        // Unlink the cell from its row
        for (Cell current = cell.D; current != cell; current = current.D) {
            for (Cell jCell = current.R; jCell != current; jCell = jCell.R) {
                jCell.D.U = jCell.U;
                jCell.U.D = jCell.D;
                jCell.C.size--;
            }
        }
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

    /**
     * Checks if a list contains any duplicate elements.
     *
     * @param arrayToCheck The list to be checked for duplicates.
     * @return True if duplicates are found, false otherwise.
     */
    public boolean hasDuplicatesList(List<Integer> arrayToCheck) {
        Set<Integer> set = new HashSet<Integer>();
        for (int x : arrayToCheck) {
            if (!set.add(x)) {
                return true; // Duplicates found
            }
        }
        return false; // No duplicates found
    }

}
