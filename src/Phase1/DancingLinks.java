package Phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
public class DancingLinks {

    public static final char[] INPUT = Search.INPUT;

    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;
    public Stack<Integer> pentIDS;
    public Set<Integer> set;
    public Stack<Integer> xos;
    public Stack<Integer> yos;
    public Stack<Integer>mutations;
    public List<Integer> coppyArray;

    public DancingLinks(int columns) {
        answer = new Stack<Integer>();
        pentIDS = new Stack<Integer>();
        set =  new HashSet<Integer> ();
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
    public void AddRow(int row,int pentId,int x0,int y0, int mutation, int[] ones) {
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

    public void algorithmX(int step) {
        // System.out.println(step);
        // if(DXSearch.count>10){return;}

        if (root.R == root) {
            System.out.println("Solution found.");
            DXSearch.count++;
            for(Integer x : pentIDS) //TODO: IMPLEMENT IDS WITH MUTATIONS AND X0 Y0 VISUALISATION
            {
                set.add(x);
                coppyArray.add(x);
            }
            if(set.size() == INPUT.length)
            {
                for(Integer y: coppyArray)
                {
                    System.out.print(y);
                }
                System.out.println();
                for(Integer y: mutations)
                {
                    System.out.print(y);
                }
                System.out.println();
            }
            coppyArray.clear();
            return;
        }

        Header head = (Header) root.R;
        int minSize = head.size;
        for(Cell xCell = head; xCell!=root; xCell = xCell.R) // optimising branch factor
        {
            if(((Header)xCell).size < minSize)
            {
                minSize = ((Header)xCell).size;
                head = (Header)xCell;

                if(head.C.size == 0){return;}
            }
        }
        cover(head);
//pana aici merge
        for (Cell rCell = head.D; rCell != head; rCell = rCell.D) 
        {
            answer.push(rCell.row);
            pentIDS.push(rCell.pentID);
            xos.push(rCell.x0);
            yos.push(rCell.y0);
            mutations.push(rCell.mutation);
            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R)
                {cover(jCell.C);}

            algorithmX(step + 1);
            answer.pop();
            pentIDS.pop();
            xos.pop();
            yos.pop();
            mutations.pop();

            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L)
                {uncover(jCell.C);}
        }
        uncover(head);
    }

    private void cover(Header head) {
        head.R.L = head.L;
        head.L.R = head.R;

        for (Cell iCell = head.D; iCell != head; iCell = iCell.D){
            for (Cell jCell = iCell.R; iCell!=jCell; jCell = jCell.R) {
                jCell.D.U = jCell.U;
                jCell.U.D = jCell.D;
                jCell.C.size--;
            }
        }
    }

    private void uncover(Header head) {
        for (Cell iCell = head.U; iCell != head; iCell = iCell.U)
            for (Cell jCell = iCell.L; jCell!=iCell; jCell = jCell.L)
            {
                jCell.D.U = jCell;
                jCell.U.D = jCell;
                jCell.C.size++;
            }
            head.R.L=head;
            head.L.R=head;
    }

    // public static void main(String[] args) {
    //     DancingLinks dance = new DancingLinks(12);

    //     dance.AddRow(0, new int[] { 3,6,7});
    //     dance.AddRow(1, new int[] { 7,10,11});
    //     dance.AddRow(2, new int[] { 6,9,10 });
    //     dance.AddRow(3, new int[] { 0,1,3 });

    //     dance.AddRow(4, new int[] { 5,6,10 });
    //     dance.AddRow(5, new int[] { 4,5,8 });
    //     dance.AddRow(6, new int[] { 2,5,6 });
    //     dance.AddRow(7, new int[] { 2,6,7 });

    //     dance.AddRow(8, new int[] { 0,1,2 });
    //     dance.AddRow(9, new int[] { 4,8,9 });
    //     dance.AddRow(10, new int[] { 6,7,11 });
    //     dance.AddRow(11, new int[] { 6,10,11 });

    //     dance.algorithmX(0);
    // }
}
