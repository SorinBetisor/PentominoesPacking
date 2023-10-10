package Phase1;
public class BruteForceSearch {
    private static final char[] INPUT = Search.INPUT;
	private static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
	private static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
	private static final UI ui = Search.ui;

    public static void main(String[] args) {
        bruteForceSearchWrapper(Search.field);
    }

    /**
	 * Wrapper function to initiate the brute force search algorithm.
	 * This function initializes the search by invoking the recursive
	 * brute force search function with the initial parameters.
	 * Note!!: Works for grids of the area of max 50 (WIDTH x HEIGHT)
	 * 
	 * @param field The matrix representing the board to be filled with pentominoes.
	 */
	public static void bruteForceSearchWrapper(int[][] field) {
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
	 */

	 public static int count = 0;
	private static boolean bruteForceSearch(int[][] field, int i) {
		count++;
		if (i == INPUT.length) {
			// All pentominoes have been placed on the board
			return true;
		}

		int pentID = Search.characterToID(INPUT[i]);
		for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
			for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
				for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
					int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
					if (Search.canPlacePiece(field, pieceToPlace, x, y)) {
						Search.addPiece(field, pieceToPlace, i, x, y);
						// try{
						// ui.setState(field);
						// Thread.sleep(1);}
						// catch(Exception ie){System.out.println(1);}
						if (bruteForceSearch(field, i + 1)) {
							return true;
						}
						Search.removePiece(field, pieceToPlace, x, y);
					}
				}
			}
		}
		return false;
	}
}
