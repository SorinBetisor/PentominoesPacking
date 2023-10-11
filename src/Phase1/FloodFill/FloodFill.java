package Phase1.FloodFill;

/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

import java.util.Arrays;

import Phase1.PentominoDatabase;
import Phase1.Search;
import Phase1.UI;

/**
 * This class includes the methods to support the search of a solution.
 */
public class FloodFill {
    public static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
    public static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
    public static final char[] INPUT = Search.INPUT;

    // Static UI class to display the board
    public static UI ui = Search.ui;

    /**
     * Helper function which starts a basic search algorithm
     */

    public static void floodFillWrapper() {
        int[][] field = Search.field;
        Search.emptyBoard(field);
        recursive(field, 0);
    }

    private static boolean recursive(int[][] field, int piecePlace) {
        if (piecePlace == INPUT.length) {
            ui.setState(field);
            return true;
        }

        int pentID = Search.characterToID(INPUT[piecePlace]);
        for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
            for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
                for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
                    try{Thread.sleep(1);}
                    catch(Exception ie){}
                    int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
                    if (Search.canPlacePiece(field, pieceToPlace, x, y)) {
                        Search.addPiece(field,pieceToPlace,pentID,x,y);
                        if (checkIsland(field)) {
                            if (recursive(field, piecePlace + 1)) {
                                return true;
                            } else {
                                clearPentomino(field, pentID, x, y, mutation);
                            }
                        } else {
                            clearPentomino(field, pentID, x, y, mutation);
                        }
                    }
                    // try{Thread.sleep(10);}
                    // catch(Exception ie){}
                }
            }
        }

        return false;
    }

    public static boolean checkIsland(int[][] field) {
        int occupiedArea = 1;
        int remainderCell = 0;

        int[][] measurefield = new int[field.length][field[0].length];
        for (int i = 0; i < measurefield.length; i++) {
            measurefield[i] = Arrays.copyOf(field[i], field[i].length);
        }

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                remainderCell = flood(measurefield, i, j, occupiedArea);
                if (remainderCell % 5 != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int flood(int[][] field, int x, int y, int occupiedArea) {
        if (x < 0 || field.length <= x || y < 0 || field[0].length <= y || field[x][y] != -1) {
            return 0;
        }

        field[x][y] = 0;
        occupiedArea = 1;
        occupiedArea += flood(field, x + 1, y, occupiedArea); // Right
        occupiedArea += flood(field, x, y + 1, occupiedArea); // Up
        occupiedArea += flood(field, x - 1, y, occupiedArea); // Left
        occupiedArea += flood(field, x, y - 1, occupiedArea); // Down

        return occupiedArea;
    }

    private static void clearPentomino(int[][] field, int pentID, int x, int y, int m) {
        int[][] piece = PentominoDatabase.data[pentID][m];

        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] == 1) {
                    field[x + i][y + j] = -1;
                }
            }
        }
        ui.setState(field);

    }

    /**
     * Main function. Needs to be executed to start the basic search algorithm
     */
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        floodFillWrapper();
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime / 1000000);
    }
}
