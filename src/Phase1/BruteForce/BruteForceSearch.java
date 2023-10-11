package Phase1.BruteForce;

import Phase1.PentominoDatabase;
import Phase1.Search;
import Phase1.UI;

/**
 * This class includes the methods to support the search of a solution using a
 * Pruned Brute Force algorithm.
 */
public class BruteForceSearch {
	public static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
	public static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
	public static int[][] field = Search.field;
	public static final char[] INPUT = Search.INPUT;
	public static UI ui = Search.ui;

	/**
	 * Recursively attempts to find a solution using the Brute Force algorithm.
	 *
	 * @param field The game board.
	 * @param ix    The index of the pentomino to place.
	 * @return True if a solution is found, false otherwise.
	 */
	private static boolean bruteForceSearch(int[][] field, int ix) {
		if (ix >= INPUT.length)
			return true;
		int pentID = Search.characterToID(INPUT[ix]);
		for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
			yCycle: for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
				mCycle: for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
					switch (pentID) {
						case 0:
							mutation = PentominoDatabase.data[pentID].length - 1;
							break;
						case 1: {
							if (mutation > 1) {
								continue;
							}
						}
						case 2: {
							if ((mutation > 2 && mutation < 4) || (mutation > 6 && mutation < 8)) {
								continue mCycle;
							} else if (mutation > 3) {
								continue yCycle;
							}
						}
							break;
						default:
							break;

					}
					int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
					if (Search.canPlacePiece(field, pieceToPlace, x, y)) {
						Search.addPiece(field, pieceToPlace, pentID, x, y);
						if (!(checkSpecialCase(field))) {
							Search.removePiece(field, pieceToPlace, x, y);
							continue yCycle;
						}
						if (bruteForceSearch(field, ix + 1)) {
							ui.setState(field);
							return true;
						}
						Search.removePiece(field, pieceToPlace, x, y);

						ui.setState(field);
						try {
							Thread.sleep(1);
						} catch (Exception ie) {
						}
					}
				}
			}
		}
		return false;

	}

	/**
	 * Checks for a special case to optimize the Brute Force algorithm.
	 *
	 * @param field The game board.
	 * @return True if the special case is met, false otherwise.
	 */
	public static boolean checkSpecialCase(int[][] field) {
		for (int xCor = 0; xCor < HORIZONTAL_GRID_SIZE; xCor++) {
			for (int c = 0; c < VERTICAL_GRID_SIZE; c++) {
				if (field[xCor][c] == -1) {
					int value = 0;

					if (xCor > 0) {
						if (field[xCor - 1][c] > 0) {
							value++;
						}
					}

					if (c > 0) {
						if (field[xCor][c - 1] > 0) {
							value++;
						}
					}

					if (xCor < HORIZONTAL_GRID_SIZE - 1) {
						if (field[xCor + 1][c] > 0) {
							value++;
						}
					}

					if (c < VERTICAL_GRID_SIZE - 1) {
						if (field[xCor][c + 1] > 0) {
							value++;
						}
					}

					if ((xCor == 0 || xCor == HORIZONTAL_GRID_SIZE - 1) && (c == 0 || c == VERTICAL_GRID_SIZE - 1)) {
						value += 2;
					} else if ((xCor == 0 || xCor == HORIZONTAL_GRID_SIZE - 1)
							|| (c == 0 || c == VERTICAL_GRID_SIZE - 1)) {
						value++;
					}

					if (value == 4) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Initiates a search for a solution using the Brute Force algorithm by clearing
	 * the game board and
	 * invoking the recursive Brute Force search function.
	 *
	 * @return True if a solution is found, false otherwise.
	 */
	public static boolean search() {
		Search.emptyBoard(field);
		if (bruteForceSearch(field, 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Initiates the Brute Force search algorithm and prints the result.
	 */
	public static void startBFSearch() {
		double start = System.currentTimeMillis();
		if (search()) {
			System.out.println("Brute Force found a solution.");
		} else {
			System.out.println("Not possible");
		}
		double end = System.currentTimeMillis();
		System.out.println("Execution time (UI updating included): " + (end - start) / 1000);
	}

	/**
	 * Executes the Brute Force search algorithm and measures execution time.
	 *
	 * @param args Command-line arguments (not used).
	 * @throws InterruptedException If the thread sleep encounters an error.
	 */
	public static void main(String[] args) throws InterruptedException {
		startBFSearch();
	}

}