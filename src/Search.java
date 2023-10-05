
/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

import java.util.Random;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Search {
	public static final int HORIZONTAL_GRID_SIZE = 6;
	public static final int VERTICAL_GRID_SIZE = 5;

	public static final char[] INPUT =
			// public static char[] input =

			{ 'U', 'Y', 'P', 'T', 'Z', 'L' };
	// {'P','X','F','V','W','Y','T','Z','U','N','L','I'};
	public static final boolean[] wasUsed = new boolean[INPUT.length - 1];

	// Static UI class to display the board
	public static UI ui = new UI(HORIZONTAL_GRID_SIZE, VERTICAL_GRID_SIZE, 50);

	/**
	 * Helper function which starts a basic search algorithm
	 * 
	 * @throws InterruptedException
	 */
	public static void search() throws InterruptedException {
		// Initialize an empty board
		int[][] field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];

		if (INPUT.length != HORIZONTAL_GRID_SIZE * VERTICAL_GRID_SIZE / 5) {
			System.out.println("Solution can't be found. Either the pieces are too many, or the grid too small");
			return;
		}

		emptyBoard(field);
		// Start the basic search
		// randomSearch(field);

		bruteForceSearchWrapper(field);
	}

	/**
	 * Get as input the character representation of a pentomino and translate it
	 * into its corresponding numerical value (ID)
	 * 
	 * @param character a character representating a pentomino
	 * @return the corresponding ID (numerical value)
	 */
	private static int characterToID(char character) {
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
	 * Basic implementation of a search algorithm. It is not a bruto force
	 * algorithms (it does not check all the posssible combinations)
	 * but randomly takes possible combinations and positions to find a possible
	 * solution.
	 * The solution is not necessarily the most efficient one
	 * This algorithm can be very time-consuming
	 * 
	 * @param field a matrix representing the board to be fulfilled with pentominoes
	 */
	private static void randomSearch(int[][] field) {
		Random random = new Random();
		boolean solutionFound = false;

		while (!solutionFound) {
			solutionFound = true;

			// Empty board again to find a solution
			emptyBoard(field);

			// Put all pentominoes with random rotation/flipping on a random position on the
			// board
			for (int i = 0; i < INPUT.length; i++) {

				// Choose a pentomino and randomly rotate/flip it
				int pentID = characterToID(INPUT[i]);
				int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

				// Randomly generate a position to put the pentomino on the board
				int x;
				int y;
				if (HORIZONTAL_GRID_SIZE < pieceToPlace.length) {
					// this particular rotation of the piece is too long for the field
					x = -1;
				} else if (HORIZONTAL_GRID_SIZE == pieceToPlace.length) {
					// this particular rotation of the piece fits perfectly into the width of the
					// field
					x = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					x = random.nextInt(HORIZONTAL_GRID_SIZE - pieceToPlace.length + 1);
				}

				if (VERTICAL_GRID_SIZE < pieceToPlace[0].length) {
					// this particular rotation of the piece is too high for the field
					y = -1;
				} else if (VERTICAL_GRID_SIZE == pieceToPlace[0].length) {
					// this particular rotation of the piece fits perfectly into the height of the
					// field
					y = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					y = random.nextInt(VERTICAL_GRID_SIZE - pieceToPlace[0].length + 1);
				}

				// If there is a possibility to place the piece on the field, do it
				if (x >= 0 && y >= 0) {
					if (canPlacePiece(field, pieceToPlace, x, y))
						addPiece(field, pieceToPlace, pentID, x, y);
				}
			}
			// Check whether complete field is filled
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					if (field[i][j] == -1) {
						solutionFound = false;
					}
				}
			}

			try {
				Thread.sleep(200);
				ui.setState(field);
				// System.out.println("Solution found");
			} catch (InterruptedException ie) {
				// display the field
				System.out.println("Solution not found");
				break;
			}
			if (solutionFound) {
				ui.setState(field);
				System.out.println("Solution found");
				break;
			}
		}
	}

	/**
	 * Wrapper function to initiate the brute force search algorithm.
	 * This function initializes the search by invoking the recursive
	 * brute force search function with the initial parameters.
	 *
	 * @param field The matrix representing the board to be filled with pentominoes.
	 * @throws InterruptedException If the execution is interrupted during sleep.
	 */
	private static void bruteForceSearchWrapper(int[][] field) throws InterruptedException {
		if (bruteForceSearch(field, 0)) {
			ui.setState(field);
			System.out.println("Solution found");
		} else {
			System.out.println("No solution found");
		}
	}

	/**
	 * Recursive brute force search for solving a pentomino puzzle.
	 * This function attempts to place pentominoes on the board by iterating through
	 * all possible positions and orientations of each pentomino.
	 *
	 * @param field The matrix representing the board to be filled with pentominoes.
	 * @param i     The index of the current pentomino to be placed.
	 * @return True if a solution is found, false otherwise.
	 * @throws InterruptedException If the execution is interrupted during sleep.
	 */
	private static boolean bruteForceSearch(int[][] field, int i) throws InterruptedException {

		if (i == INPUT.length) {
			// All pentominoes have been placed on the board
			return true;
		}

		int pentID = characterToID(INPUT[i]);
		for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
			for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
				for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
					int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
					if (canPlacePiece(field, pieceToPlace, x, y)) {
						addPiece(field, pieceToPlace, i, x, y);
						// ui.setState(field);
						// Thread.sleep(10);
						if (bruteForceSearch(field, i + 1)) {
							return true;
						}
						removePiece(field, pieceToPlace, x, y);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Adds a pentomino to the position on the field (overriding current board at
	 * that position)
	 * 
	 * @param field   a matrix representing the board to be fulfilled with
	 *                pentominoes
	 * @param piece   a matrix representing the pentomino to be placed in the board
	 * @param pieceID ID of the relevant pentomino
	 * @param x       x position of the pentomino
	 * @param y       y position of the pentomino
	 */
	public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
			{
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
	private static boolean canPlacePiece(int[][] field, int[][] piece, int x, int y) {
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

	public static int[][] emptyBoard(int[][] field) {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				// -1 in the state matrix corresponds to empty square
				// Any positive number identifies the ID of the pentomino
				field[i][j] = -1;
			}
		}
		return field;
	}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		search();

	}
}