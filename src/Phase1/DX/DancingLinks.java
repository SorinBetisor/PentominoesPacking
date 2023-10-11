package Phase1.DX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import Phase1.Search;
import Phase1.UI;



public class DancingLinks {

    public static final char[] INPUT = Search.INPUT;
    private static final UI ui = Search.ui;

    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;
    public Stack<Integer> pentIDS;
    public List<Row> rows;

    public DancingLinks(int columns) {
        answer = new Stack<Integer>();
        pentIDS = new Stack<Integer>();

        root = new Header(-1);
        headers = new Header[columns];
        for (int j = 0; j < columns; j++) {
            headers[j] = new Header(j);
            root.InsertLeft(headers[j]);
        }
    }

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

    public void algorithmX(int step) {
        if (root.R == root) {
            // if (!hasDuplicates(pentIDS)) {
            // System.out.println("Solution found");
            // for (int x : pentIDS) {
            // System.out.print(x + " ");
            // }
            // System.out.println();}
            // System.exit(100);
            boolean duplicates = false;
            List<Row> rows = new ArrayList<Row>();
            List<Integer> pentIDSList = new ArrayList<Integer>();
            for (var row : answer) {
                Row r = DXSearch.rows.get(row);
                rows.add(r);
                pentIDSList.add(r.pentID);
            }

            duplicates = hasDuplicatesList(pentIDS);
            if (!duplicates) {
                for (var row : rows) {
                    DXSearch.drawPentominoe(row.pentID, row.mutation, row.x0, row.y0);
                }
                ui.setState(Search.field);
                try{
                    Thread.sleep(500);
                }
                catch(Exception ie){}
                return;
            }}

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
