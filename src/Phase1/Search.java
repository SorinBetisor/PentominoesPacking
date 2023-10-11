package Phase1;

import java.util.Scanner;

import Phase1.BruteForce.BruteForceSearch;
import Phase1.DX.DXSearch;
import Phase1.FloodFill.FloodFill;
import Phase1.RandomSearch.RandomSearch;

/**
 * This class includes the methods to support the search of a solution.
 * 
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */
public class Search {
	public static final int HORIZONTAL_GRID_SIZE = Constants.HORIZONTAL_GRID_SIZE;
	public static final int VERTICAL_GRID_SIZE = Constants.VERTICAL_GRID_SIZE;
	public static final char[] INPUT = Constants.INPUT;

	// Static UI class to display the board
	public static UI ui = new UI(HORIZONTAL_GRID_SIZE, VERTICAL_GRID_SIZE, 50);

	// Initialize an empty board
	public static int[][] field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];

	/**
	 * Helper function which starts a basic search algorithm.
	 */
	public static void search(int selectedAlgorithm) {
		if (INPUT == null || HORIZONTAL_GRID_SIZE * VERTICAL_GRID_SIZE == 0) {
			System.out.println("Solution can't be found. ");
			return;
		}

		if (INPUT.length != HORIZONTAL_GRID_SIZE * VERTICAL_GRID_SIZE / 5) {
			System.out.println("Solution can't be found. Either the pieces are too many, or the grid is too small");
			return;
		}

		emptyBoard(field);
		// Start the basic search
		selectAlgorithm(selectedAlgorithm, field);
	}

	/**
	 * Selects and executes a search algorithm based on the provided algorithm ID.
	 *
	 * @param id    The algorithm ID representing the selected search algorithm:
	 *              - 1: Random Search
	 *              - 2: Brute Force Search
	 *              - 3: DX Search
	 *              - 4: Flood Fill
	 * @param field A matrix representing the board on which the search algorithm
	 *              operates.
	 */
	public static void selectAlgorithm(int id, int[][] field) {
		if (id == 1) {
			RandomSearch.randomSearch(field);
		} else if (id == 2) {
			BruteForceSearch.startBFSearch();
		} else if (id == 3) {
			DXSearch.dxSearch();
		} else if (id == 4) {
			FloodFill.floodFillWrapper();
		} else {
			System.out.println("Invalid algorithm ID.");
		}
	}

	/**
	 * Get as input the character representation of a pentomino and translate it
	 * into its corresponding numerical value (ID).
	 * 
	 * @param character a character representing a pentomino
	 * @return the corresponding ID (numerical value)
	 */
	public static int characterToID(char character) {
		int pentID = -1;
		if (character == 'X') {
			pentID = 0;
		} else if (character == 'I') {
			pentID = 1;
		} else if (character == 'Z') {
			pentID = 2;
		} else if (character == 'T') {
			pentID = 3;
		} else if (character == 'U') {
			pentID = 4;
		} else if (character == 'V') {
			pentID = 5;
		} else if (character == 'W') {
			pentID = 6;
		} else if (character == 'Y') {
			pentID = 7;
		} else if (character == 'L') {
			pentID = 8;
		} else if (character == 'P') {
			pentID = 9;
		} else if (character == 'N') {
			pentID = 10;
		} else if (character == 'F') {
			pentID = 11;
		}
		return pentID;
	}

	/**
	 * Adds a pentomino to the position on the field (overriding the current board
	 * at
	 * that position).
	 * 
	 * @param field   a matrix representing the board to be fulfilled with
	 *                pentominoes
	 * @param piece   a matrix representing the pentomino to be placed on the board
	 * @param pieceID ID of the relevant pentomino
	 * @param x       x position of the pentomino
	 * @param y       y position of the pentomino
	 */
	public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					// Add the ID of the pentomino to the board if the pentomino occupies this
					// square
					field[x + i][y + j] = pieceID;
				}
			}
		}
	}

	/**
	 * Removes a pentomino from the position on the field.
	 * 
	 * @param field a matrix representing the board with pentominoes
	 * @param piece a matrix representing the pentomino to be removed
	 * @param x     x position of the pentomino to be removed
	 * @param y     y position of the pentomino to be removed
	 */
	public static void removePiece(int[][] field, int[][] piece, int x, int y) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					// Remove the pentomino from the board if it occupies this square
					field[x + i][y + j] = -1;
				}
			}
		}
	}

	/**
	 * Check if a pentomino piece can be placed at a given position on the field.
	 * 
	 * @param field a matrix representing the board
	 * @param piece a matrix representing the pentomino piece
	 * @param x     x position on the field
	 * @param y     y position on the field
	 * @return true if the piece can be placed, false otherwise
	 */
	public static boolean canPlacePiece(int[][] field, int[][] piece, int x, int y) {
		// Check if the piece can be placed within the boundaries of the field
		if (x + piece.length > HORIZONTAL_GRID_SIZE) {
			return false;
		}
		for (int i = 0; i < piece.length; i++) {
			if (y + piece[i].length > VERTICAL_GRID_SIZE)
				return false;
		}

		// Check if the cells for the piece are empty
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] >= 1 && field[x + i][y + j] != -1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Initializes and returns an empty game board represented as a matrix, setting
	 * all cells to -1.
	 *
	 * @param field The matrix representing the game board where each cell can have
	 *              one of the following values:
	 *              -1: Represents an empty square on the board.
	 *              N: Represents the ID of a pentomino occupying the square (N is a
	 *              positive number).
	 * @return An empty game board where all cells are set to -1 (representing empty
	 *         squares).
	 */
	public static int[][] emptyBoard(int[][] field) {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				// -1 in the state matrix corresponds to an empty square
				// Any positive number identifies the ID of the pentomino
				field[i][j] = -1;
			}
		}
		return field;
	}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Select an algorithm:");
        System.out.println("1. Random Search");
        System.out.println("2. Pruned Brute Force");
        System.out.println("3. Dancing Links Algorithm X");
        System.out.println("4. Flood Fill");

        System.out.print("Enter the desired algorithm number: ");
		int algID = scanner.nextInt();
		scanner.close();
		search(algID);
	}
}
