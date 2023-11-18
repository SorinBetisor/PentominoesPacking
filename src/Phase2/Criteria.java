package Phase2;

import java.util.Arrays;

public class Criteria {

    public static int calculateHeight(int[][] field) {
        int currentHeight = 0;
        int maxHeight = 0;
        for (int j = 0; j < field[0].length; j++) {
            for (int i = 14; i >= 0; i--) {
                if (field[i][j] != -1) {
                    currentHeight = 15 - i;
                }

            }
            if (currentHeight > maxHeight) {
                maxHeight = currentHeight;
            }
            currentHeight = 0;

        }
        return maxHeight;
    }

    public static int calculateGaps(int[][] field) {
        boolean[][] checked = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                checked[i][j] = false;
            }
        }
        return calculateGaps(field, checked);
    }

    public static int calculateGaps(int[][] field, boolean[][] checked) {
        int gaps = -1;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (checked[i][j])
                    continue;
                if (field[i][j] != -1) {
                    checked[i][j] = true;
                    continue;
                }
                gaps++;
                floodFillHoles(i, j, field, checked);
            }
        }
        return gaps;
    }

    private static void floodFillHoles(int row, int col, int[][] field, boolean[][] checked) {
        if (row < 0 || row >= field.length || col < 0 || col >= field[0].length)
            return;
        if (checked[row][col] || field[row][col] != -1)
            return;
        checked[row][col] = true;
        floodFillHoles(row + 1, col, field, checked);
        floodFillHoles(row - 1, col, field, checked);
        floodFillHoles(row, col + 1, field, checked);
        floodFillHoles(row, col - 1, field, checked);
    }

    public static boolean canClearRow(int[][] field)
    {
        for (int i = 0; i < field.length; i++) {
            boolean canClear = true;
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == -1) {
                    canClear = false;
                    break;
                }
            }
            if (canClear)
                return true;
        }
        return false;
    }
    

    public static void main(String[] args) {

        int[][] matrix = {
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1,  0, -1, -1, -1},
            { 0,  0,  0, -1, -1},
            {-1,  0,  9,  9,  9},
            {-1, -1, -1,  9,  9},
            { 7,  7,  7,  7, -1},
            {-1, -1,  7, -1, -1}
        };
        

        // System.out.println(calculateHeight(matrix));
        System.out.println(calculateGaps(matrix));
    }
}
