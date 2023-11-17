package Phase2;

public class Criteria {

    public static int calculateHeight(int[][] field) {
        int currentHeight = 0;
        int maxHeight = 0;
        for (int j = 0; j < field[0].length; j++) {
            for (int i = 14; i >= 0; i--) {
                if (field[i][j] !=-1)
                {
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


    //TODO: implement counting gaps
    public static int calculateGaps(int[][] field)
    {
        floodFillHoles(field, 0, 0);
        return -1;
    }

    private static boolean floodFillHoles(int[][] field, int x, int y)
    {
        return true;
    }

    public static void main(String[] args) {

        int[][] matrix = {
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, 1, -1 },
                { -1, -1, 10, -1, -1 },
                { -1, -1, 1, -1, -1 },
                { -1, 1, 1, 1, -1 },
                { -1, -1, 1, 1, -1 }
        };

        System.out.println(calculateHeight(matrix));
    }
}
