package Phase2.helperClasses;

/**
 * The Matrix class provides utility methods for manipulating 2D matrices.
 */
public class Matrix {
     /**
     * Rotates a given matrix by 90 degrees clockwise.
     * 
     * @param matrix the matrix to be rotated
     * @return the rotated matrix
     */
    public static int[][] rotateMatrix(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] rotatedMatrix = new int[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rotatedMatrix[j][numRows - 1 - i] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    /**
     * Rotates the given matrix 90 degrees counter-clockwise.
     * 
     * @param matrix the matrix to be rotated
     * @return the rotated matrix
     */
    public static int[][] rotateMatrixBack(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] rotatedMatrix = new int[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rotatedMatrix[j][i] = matrix[i][numCols - 1 - j];
            }
        }

        return rotatedMatrix;
    }

    
    /** 
     * @param field
     * @return int[][]
     */
    public static int[][] wipeField(int[][] field)
    {
        int[][] newField = new int[field.length][field[0].length];
        for(int i = 0; i < field.length; i++)
        {
            for(int j = 0; j < field[0].length; j++)
            {
                newField[i][j] = -1;
            }
        }
        return newField;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * 
     * @param field the 2D integer array to be copied
     * @return a new 2D integer array that is a deep copy of the input array
     */
    public static int[][] deepCopy(int[][] field) {
        int[][] copy = new int[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, field[0].length);
        }
        return copy;
    }
}
