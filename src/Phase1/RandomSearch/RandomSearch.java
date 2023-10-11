package Phase1.RandomSearch;

import Phase1.PentominoDatabase;
import Phase1.Search;
import Phase1.UI;

public class RandomSearch {
	private static final char[] INPUT = Search.INPUT;
	private static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
	private static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
	private static final UI ui = Search.ui;

	public static void main(String[] args) throws InterruptedException {
		randomSearch(Search.field);
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

	public static void randomSearch(int[][] field) {
		java.util.Random random = new java.util.Random();
		boolean solutionFound = false;

		while (!solutionFound) {
			solutionFound = true;

			// Empty board again to find a solution
			Search.emptyBoard(field);

			// Put all pentominoes with random rotation/flipping on a random position on the
			// board
			for (int i = 0; i < INPUT.length; i++) {

				// Choose a pentomino and randomly rotate/flip it
				int pentID = Search.characterToID(INPUT[i]);
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
					if (Search.canPlacePiece(field, pieceToPlace, x, y))
						Search.addPiece(field, pieceToPlace, pentID, x, y);
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

}
