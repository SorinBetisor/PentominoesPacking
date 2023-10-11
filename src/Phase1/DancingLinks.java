package Phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DancingLinks {

    public static final char[] INPUT = Search.INPUT;
    // private static final UI ui = Search.ui;

    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;
    public Stack<Integer> pentIDS;

    public Stack<Integer> xos;
    public Stack<Integer> yos;
    public Stack<Integer> mutations;
    public List<Integer> coppyArray;

    public DancingLinks(int columns) {
        answer = new Stack<Integer>();
        pentIDS = new Stack<Integer>();
        xos = new Stack<Integer>();
        yos = new Stack<Integer>();
        coppyArray = new ArrayList<Integer>();
        mutations = new Stack<Integer>();
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
            cell.x0 = x0;
            cell.y0 = y0;
            cell.mutation = mutation;

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

    // public static <T> boolean hasDuplicates(Stack<T> stack) {
    //     Set<T> seen = new HashSet<>();
    //     for (T item : stack) {
    //         if (!seen.add(item)) {
    //             return true; // Duplicates found
    //         }
    //     }
    //     return false; // No duplicates found
    // }

    public void algorithmX(int step) { //TODO: FIX DUPLICATE SOLUTIONS
        if (root.R == root) {
            // System.out.println("Solution found");
            
            for (Integer z : pentIDS) {
                // coppyArray.add(z);
                System.out.print(z+" ");}
            System.out.println();
            // if (!hasDuplicates(coppyArray)) {
            //     System.out.println("Solution found.");}

                // List<Integer> pentIDsArr = new ArrayList<Integer>();
                // List<Integer> mutationsArr = new ArrayList<Integer>();
                // List<Integer> xArr = new ArrayList<Integer>();
                // List<Integer> ysArr = new ArrayList<Integer>();

                // for (Integer x : pentIDS) {
                //     pentIDsArr.add(x);
                //     System.out.print(x+" ");
                // }
                // System.out.println();

                // for (Integer x : mutations) {
                //     mutationsArr.add(x);
                //     System.out.print(x+" ");
                // }
                // System.out.println();
                // for (Integer x : xos) {
                //     xArr.add(x);
                //     System.out.print(x+" ");
                // }
                // System.out.println();
                // for (Integer x : yos) {
                //     ysArr.add(x);
                //     System.out.print(x+" ");
                // }
                // System.out.println();
            //     // for (int k = 0; k <= 5; k++) {
            //     //     DXSearch.drawPentominoe(pentIDsArr.get(k), mutationsArr.get(k), xArr.get(k),
            //     //             ysArr.get(k));
            //     // }


            //     // ui.setState(DXSearch.field);
                    

            //     // System.out.println(Arrays.deepToString(DXSearch.field));

            //     pentIDsArr.clear();
            //     mutationsArr.clear();
            //     xArr.clear();
            //     ysArr.clear();
            // }
            // coppyArray.clear();
            // Search.emptyBoard(DXSearch.field);

            return;
        }

        Header head = (Header) root.R;
        int minSize = head.size;
        for (Cell xCell = head; xCell != root; xCell = xCell.R) // optimising branch factor
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
            xos.push(rCell.x0);
            yos.push(rCell.y0);
            mutations.push(rCell.mutation);
            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R) {
                cover(jCell.C);
            }

            algorithmX(step + 1);
            answer.pop();
            pentIDS.pop();
            xos.pop();
            yos.pop();
            mutations.pop();

            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L) {
                uncover(jCell.C);
            }
        }
        uncover(head);
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
