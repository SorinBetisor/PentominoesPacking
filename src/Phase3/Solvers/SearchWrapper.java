package Phase3.Solvers;
/**
 * The SearchWrapper class provides utility methods for manipulating and checking the game field.
 */
public class SearchWrapper {
    //first iterate over z then y then x; this is the order in which the pieces are stored in the database

    /**
     * Adds a piece to the game field at the specified coordinates.
     * 
     * @param gameField    the game field to add the piece to
     * @param piece        the piece to be added
     * @param coordinates  the coordinates where the piece should be added
     */
    public static void addPiece(int[][][] gameField, int[][][] piece, int[] coordinates) {
        for (int z = 0; z < piece.length; z++) {
            for (int y = 0; y < piece[z].length; y++) {
                for (int x = 0; x < piece[z][y].length; x++) {
                    if (piece[z][y][x] != 0) {
                        gameField[coordinates[0] + z][coordinates[1] + y][coordinates[2] + x] = piece[z][y][x];
                    }
                }
            }
        }
    }
    

    /**
     * Checks if a given piece can be placed at the specified coordinates in the field.
     *
     * @param field           the field to check against
     * @param piece           the piece to be placed
     * @param placementCoords the coordinates where the piece is to be placed
     * @return true if the piece can be placed, false otherwise
     */
    public static boolean canPlacePiece(int[][][] field, int[][][] piece, int[] placementCoords) {
        for (int z = 0; z < piece.length; z++) {
            for (int y = 0; y < piece[z].length; y++) {
                for (int x = 0; x < piece[z][y].length; x++) {
                    if (piece[z][y][x] == 0) {
                        continue; // skips empty spaces in piece
                    }
                    if (placementCoords[0] + z >= field.length || placementCoords[1] + y >= field[z].length ||
                            placementCoords[2] + x >= field[z][y].length) {
                        return false; // out of bounds
                    }
                    if (field[placementCoords[0] + z][placementCoords[1] + y][placementCoords[2] + x] != 0) {
                        return false; // target position already occupied
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given 3D field is fully covered.
     * 
     * @param field the 3D field represented as a 3D array
     * @return true if the field is fully covered, false otherwise
     */
    public static boolean checkFullCover(int[][][] field) {
        for (int z = 0; z < field.length; z++) {
            for (int y = 0; y < field[z].length; y++) {
                for (int x = 0; x < field[z][y].length; x++) {
                    if (field[z][y][x] == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Wipes the given field by setting all elements to 0.
     *
     * @param field The 3D array representing the field.
     * @return The modified field with all elements set to 0.
     */
    public static int[][][] wipeField(int[][][] field) {
        for (int z = 0; z < field.length; z++) {
            for (int y = 0; y < field[z].length; y++) {
                for (int x = 0; x < field[z][y].length; x++) {
                    field[z][y][x] = 0;
                }
            }
        }
        return field;
    }

    /**
     * Inverts the elements of the given array in place.
     * 
     * @param arr the array to be inverted
     */
    public static void invertArray(int[] arr) {
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    
}
