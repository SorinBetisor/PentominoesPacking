package Phase1;

public class BruteForceSearch {
	public static final int horizontalGridSize = 12;
	public static final int verticalGridSize = 5;

	public static final char[] INPUT = Search.INPUT;

	// Static UI class to display the board
	public static UI ui = Search.ui;

	/**
	 * Implementation of a search algorithm for solving pentomino puzzles. This
	 * algorithm
	 * attempts to fill a board with pentominoes using a heuristic approach,
	 * exploring
	 * various combinations and positions to find a possible solution. The solution
	 * may not
	 * necessarily be the most efficient or optimal one and can be time-consuming.
	 *
	 * @param field    a matrix representing the board to be filled with pentominoes
	 * @param iterator the current pentomino piece being placed
	 * @return true if a solution is found, false otherwise
	 * @throws InterruptedException if interrupted during execution
	 */
	public static boolean bruteForceSearch(int[][] field, int iterator) throws InterruptedException {
		if (INPUT.length * 5 != horizontalGridSize * verticalGridSize) {
			return false;
		}
		if (iterator >= INPUT.length)
			return true;
		int pentID = Search.characterToID(INPUT[iterator]);
		for (int x = 0; x < horizontalGridSize; x++) {
			yCycle: for (int y = 0; y < verticalGridSize; y++) {
				mCycle: for (int rotation = 0; rotation < PentominoDatabase.data[pentID].length; rotation++) {
					//handle special cases ( corner placements )
					switch (pentID) {
						case 0:
							rotation = PentominoDatabase.data[pentID].length - 1;
							break;
						case 3:
						case 5:
						case 6:
						case 1:
						case 10:
						case 4:
						case 2:
							if (pentID == 1) {
								if (rotation > 1) {
									continue;
								}
							} else if (pentID == 2) {
								if ((rotation > 2 && rotation < 4) || (rotation > 6 && rotation < 8)) {
									continue mCycle;
								}
							} else if (rotation > 3) {
								continue yCycle;
							}
							break;
							default: break;
					}

					int[][] pieceToPlace = PentominoDatabase.data[pentID][rotation];
					if (Search.canPlacePiece(field, pieceToPlace, x, y)) {
						Search.addPiece(field, pieceToPlace, pentID, x, y);
						if (!(checkCornerPlacement(field))) {
							Search.removePiece(field, pieceToPlace, x, y);
							continue yCycle;
						}
						if (bruteForceSearch(field, iterator + 1)) {
							// ui.setState(field);
							return true;
						}
						Search.removePiece(field, pieceToPlace, x, y);

						ui.setState(field);
						// Thread.sleep(1000);
					}
				}
			}
		}
		return false;

	}

	public static boolean checkCornerPlacement(int[][] field) {
		for (int j = 0; j < horizontalGridSize; j++) {
			for (int c = 0; c < verticalGridSize; c++) {
				int value = 0;
				if (field[j][c] == -1) {
					if (j == 0) {
						if (c == 0) {
							value += 2;
							if (field[j + 1][c] > 0) {
								value++;
							}
							if (field[j][c + 1] > 0) {
								value++;
							}
						} else if (c == verticalGridSize - 1) {
							value += 2;
							if (field[j + 1][c] > 0) {
								value++;
							}
							if (field[j][c - 1] > 0) {
								value++;
							}
						} else {
							value++;
							if (field[j + 1][c] > 0) {
								value++;
							}
							if (field[j][c - 1] > 0) {
								value++;
							}
							if (field[j][c + 1] > 0) {
								value++;
							}
						}
					} else if (j == horizontalGridSize - 1) {
						if (c == 0) {
							value += 2;
							if (field[j - 1][c] > 0) {
								value++;
							}
							if (field[j][c + 1] > 0) {
								value++;
							}
						} else if (c == verticalGridSize - 1) {
							value += 2;
							if (field[j - 1][c] > 0) {
								value++;
							}
							if (field[j][c - 1] > 0) {
								value++;
							}
						} else {
							value++;
							if (field[j - 1][c] > 0) {
								value++;
							}
							if (field[j][c - 1] > 0) {
								value++;
							}
							if (field[j][c + 1] > 0) {
								value++;
							}
						}
					} else if (c == 0) {
						value++;
						if (field[j + 1][c] > 0) {
							value++;
						}
						if (field[j - 1][c] > 0) {
							value++;
						}
						if (field[j][c + 1] > 0) {
							value++;
						}
					} else if (c == verticalGridSize - 1) {
						value++;
						if (field[j + 1][c] > 0) {
							value++;
						}
						if (field[j - 1][c] > 0) {
							value++;
						}
						if (field[j][c - 1] > 0) {
							value++;
						}
					} else {
						if (field[j + 1][c] > 0) {
							value++;
						}
						if (field[j][c + 1] > 0) {
							value++;
						}
						if (field[j - 1][c] > 0) {
							value++;
						}
						if (field[j][c - 1] > 0) {
							value++;
						}
					}
				}
				if (value == 4) {
					return false;
				}
			}
		}
		return true;
	}
}