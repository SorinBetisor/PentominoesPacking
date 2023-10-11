package Phase1;

import java.util.ArrayList;
import java.util.List;

public class DXSearch {
	private static final char[] INPUT = Search.INPUT;
	private static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
	private static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
	public static int[][] field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];
	public static UI ui = Search.ui;

	public static void main(String[] args) {
		Search.emptyBoard(field);
		DancingLinks dance = new DancingLinks(HORIZONTAL_GRID_SIZE * VERTICAL_GRID_SIZE);
		int nr = 0;
		for (int i = 0; i < INPUT.length; i++) {
			int pentID = characterToID(INPUT[i]);
			for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
				for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
					for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
						boolean can = canPlacePieceBool(pieceToPlace, x, y);

						if (!can) {
							continue;
						}

						List<Integer> xs = getOccupiedCellsX(pieceToPlace, x, y);
						List<Integer> ys = getOccupiedCellsY(pieceToPlace, x, y);
						dance.AddRow(nr, pentID, x, y, mutation, new int[] {
								xs.get(0) + HORIZONTAL_GRID_SIZE * (ys.get(0)),
								xs.get(1) + HORIZONTAL_GRID_SIZE * (ys.get(1)),
								xs.get(2) + HORIZONTAL_GRID_SIZE * (ys.get(2)),
								xs.get(3) + HORIZONTAL_GRID_SIZE * (ys.get(3)),
								xs.get(4) + HORIZONTAL_GRID_SIZE * (ys.get(4)),
						});
						// System.out.println(nr + " ID" + pentID + " M" + mutation + " X" + x + " Y" +
						// y); // y?
						nr++;
					}
				}
			}
		}
		dance.algorithmX(0);
		// drawPentominoe(8, 6, 0, 0);
		// drawPentominoe(3, 2, 2, 0);
		// drawPentominoe(2, 2, 1, 1);
		// drawPentominoe(9, 3, 0, 2);
		// drawPentominoe(1, 0, 0, 5);
		// drawPentominoe(4, 2, 2, 3);
		// ui.setState(field);
	}

	public static int count = 0;

	public static boolean canPlacePieceBool(int[][] piece, int x, int y) {
		// Check if the piece can be placed within the boundaries of the field

		if (x + piece.length > HORIZONTAL_GRID_SIZE) {
			return false;
		}
		for (int i = 0; i < piece.length; i++) {
			if (y + piece[i].length > VERTICAL_GRID_SIZE)
				return false;
		}
		return true;
	}

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

	public static List<Integer> getOccupiedCellsX(int[][] pieceToplace, int row, int col) {
		List<Integer> arrX = new ArrayList<>();
		int distanceX = -1;
		for (int x = 0; x < pieceToplace.length; x++) {
			for (int j = 0; j < pieceToplace[0].length; j++) {
				if (pieceToplace[x][j] != 0 && j == 0) {
					distanceX = x;
					break; // Exit the inner loop once the first occupied cell is found
				}
			}
			if (distanceX != -1) {
				break; // Exit the outer loop once the first occupied cell is found
			}
		}

		for (int x = 0; x < pieceToplace.length; x++) {
			for (int j = 0; j < pieceToplace[0].length; j++) {
				if (pieceToplace[x][j] != 0) {
					arrX.add(x + row);
				}
			}
		}
		return arrX;
	}

	public static List<Integer> getOccupiedCellsY(int[][] pieceToplace, int row, int col) {
		List<Integer> arrY = new ArrayList<>();
		int distanceY = -1;
		for (int x = 0; x < pieceToplace.length; x++) {
			for (int j = 0; j < pieceToplace[0].length; j++) {
				if (pieceToplace[x][j] != 0 && x == 0) {
					distanceY = j;
					break; // Exit the inner loop once the first occupied cell is found
				}
			}
			if (distanceY != -1) {
				break; // Exit the outer loop once the first occupied cell is found
			}
		}

		for (int x = 0; x < pieceToplace.length; x++) {
			for (int j = 0; j < pieceToplace[0].length; j++) {
				if (pieceToplace[x][j] != 0) {
					arrY.add(j + col);
				}
			}
		}
		return arrY;
	}

	public static void drawPentominoe(int pentID, int mutation, int x0, int y0) {
		int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
		// System.out.println(Arrays.deepToString(pieceToPlace));
		for (int x = 0; x < pieceToPlace.length; x++) {
			for (int j = 0; j < pieceToPlace[0].length; j++) {
				if (pieceToPlace[x][j] != 0) {
					field[x + x0][j + y0] = pentID;
					// try{Thread.sleep(20);}
					// catch(Exception ie){System.out.println(1);}
				}
			}
		}
	}
}
