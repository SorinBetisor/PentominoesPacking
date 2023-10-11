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
    public static double start;

    // Static UI class to display the board
    public static UI ui = Search.ui;

    /**
     * Helper function which starts the Flood Fill search algorithm.
     */
    public static void floodFillWrapper() {
        int[][] field = Search.field;
        Search.emptyBoard(field);
        start = System.currentTimeMillis();
        recursive(field, 0);
    }

    /**
     * Recursively attempts to place pentominoes on the board and find a solution.
     *
     * @param field      The game board.
     * @param piecePlace The index of the pentomino to place.
     * @return True if a solution is found, false otherwise.
     */
    private static boolean recursive(int[][] field, int piecePlace) {
        if (piecePlace == INPUT.length) {
            ui.setState(field);
            double end = System.currentTimeMillis();
		    System.out.println("(Flood Fill) Execution time (UI updating included): " + (end - start) / 1000);
            return true;
        }

        int pentID = Search.characterToID(INPUT[piecePlace]);
        for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
            for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
                for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
                    int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
                    if (Search.canPlacePiece(field, pieceToPlace, x, y)) {
                        Search.addPiece(field, pieceToPlace, pentID, x, y);
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

    /**
     * Checks if the game board is divided into islands, ensuring proper placement
     * of pentominoes.
     *
     * @param field The game board.
     * @return True if the board is divided into islands correctly, false otherwise.
     */
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

    /**
     * Flood-fill algorithm to measure the size of an island.
     *
     * @param field        The game board.
     * @param x            The x-coordinate to start the flood-fill.
     * @param y            The y-coordinate to start the flood-fill.
     * @param occupiedArea The size of the island.
     * @return The size of the island after flood-fill.
     */
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

    /**
     * Clears a pentomino from the board.
     *
     * @param field  The game board.
     * @param pentID The ID of the pentomino to clear.
     * @param x      The x-coordinate of the pentomino.
     * @param y      The y-coordinate of the pentomino.
     * @param m      The mutation of the pentomino.
     */
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
