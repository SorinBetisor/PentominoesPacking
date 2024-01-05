package Phase3.Solvers;
public class SearchWrapper {
    //first iterate over z then y then x; this is the order in which the pieces are stored in the database

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

    
}
