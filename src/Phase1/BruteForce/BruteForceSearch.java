package Phase1.BruteForce;

import Phase1.PentominoDatabase;
import Phase1.Search;
import Phase1.UI;

public class BruteForceSearch {
	public static final int horizontalGridSize = Search.HORIZONTAL_GRID_SIZE;
	public static final int verticalGridSize = Search.VERTICAL_GRID_SIZE;
	public static int[][] field = Search.field;
	public static final char[] INPUT = Search.INPUT;
	public static UI ui = Search.ui;

	private static boolean bruteForceSearch(int[][] field, int ix) {
		if (ix >= INPUT.length)
			return true;
		int pentID = Search.characterToID(INPUT[ix]);
		for (int x = 0; x < horizontalGridSize; x++) {
			yCycle: for (int y = 0; y < verticalGridSize; y++) {
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

	public static boolean checkSpecialCase(int[][] field) {
		for (int xCor = 0; xCor < horizontalGridSize; xCor++) {
			for (int c = 0; c < verticalGridSize; c++) {
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

					if (xCor < horizontalGridSize - 1) {
						if (field[xCor + 1][c] > 0) {
							value++;
						}
					}

					if (c < verticalGridSize - 1) {
						if (field[xCor][c + 1] > 0) {
							value++;
						}
					}

					if ((xCor == 0 || xCor == horizontalGridSize - 1) && (c == 0 || c == verticalGridSize - 1)) {
						value += 2;
					} else if ((xCor == 0 || xCor == horizontalGridSize - 1) || (c == 0 || c == verticalGridSize - 1)) {
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

	public static boolean search() {
		Search.emptyBoard(field);
		if (bruteForceSearch(field, 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static void startBFSearch() {
		double start = System.currentTimeMillis();
		if (search()) {
			System.out.println("Brute Force found a solution.");
		} else {
			System.out.println("Not possible");
		}
		double end = System.currentTimeMillis();
		System.out.println("Execution time: " + (end - start) / 1000);
	}

	/**
	 * Main function. Needs to be executed to start the basic isSearch algorithm
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		startBFSearch();
	}

}