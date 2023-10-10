package Phase1;

import java.util.Stack;
public class DancingLinks {
    public Header root;
    public Header[] headers;
    public Stack<Integer> answer;

    public DancingLinks(int columns) {
        answer = new Stack<Integer>();
        root = new Header(-1);
        headers = new Header[columns];
        for (int j = 0; j < columns; j++) {
            headers[j] = new Header(j);
            root.InsertLeft(headers[j]);
        }
    }

    // accepts indexes of one only in increasing order!
    public void AddRow(int row, int[] ones) {
        int last = -1;
        Cell first = null;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]);
            headers[x].InsertUp(cell);
            cell.row = row;

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
        if (root.R == root) {
            System.out.println("Solution found.");
            for(Integer x : answer)
            {
                System.out.print(x + " ");
            }
            System.out.println();
            return;
        }

        Header head = (Header) root.R;
        int minSize = head.size;
        for(Cell xCell = head; xCell!=root; xCell = xCell.R) // optimising branch factor
        {
            if(((Header)xCell).size < minSize && ((Header)xCell).size>0)
            {
                minSize = ((Header)xCell).size;
                head = (Header)xCell;
            }
        }
        cover(head);
//pana aici merge
        for (Cell rCell = head.D; rCell != head; rCell = rCell.D) 
        {
            answer.push(rCell.row);
            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R)
                {cover(jCell.C);}

            algorithmX(step + 1);
            answer.pop();

            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L)
                {uncover(jCell.C);}
        }
        uncover(head);
    }

    private void cover(Header head) {
        head.R.L = head.L;
        head.L.R = head.R;

        for (Cell iCell = head.D; iCell != head; iCell = iCell.D) {
            for (Cell jCell = iCell.R; iCell!=jCell; jCell = jCell.R) {
                jCell.D.U = jCell.U;
                jCell.U.D = jCell.D;
                jCell.C.size--;
            }
        }
    }

    private void uncover(Header head) {
        for (Cell iCell = head.U; iCell != head; iCell = iCell.U)
            for (Cell jCell = iCell.L; iCell != jCell; jCell = jCell.L)
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
