package Phase1.DX;

import java.util.ArrayList;
import java.util.List;

import Phase1.PentominoDatabase;
import Phase1.Search;
import Phase1.UI;

/**
 * DXSearch is a class that contains methods for solving pentomino puzzles using
 * the Dancing Links algorithm.
 */
public class DXSearch {
	private static final char[] INPUT = Search.INPUT;
	private static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
	private static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;
	public static int[][] field = Search.field;
	public static UI ui = Search.ui;

	public static List<Row> rows = new ArrayList<Row>();

	/**
	 * Solves a pentomino puzzle using the Dancing Links algorithm.
	 */
	public static void dxSearch() {
		Search.emptyBoard(field);
		DancingLinks dance = new DancingLinks(HORIZONTAL_GRID_SIZE * VERTICAL_GRID_SIZE);
		int nr = 0;
		for (int i = 0; i < INPUT.length; i++) {
			int pentID = Search.characterToID(INPUT[i]);
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
						dance.AddRow(nr, pentID, x, y, mutation, new int[] { //TODO: SWITCH ARRAYLISTS WITH ARRAYS
								xs.get(0) + HORIZONTAL_GRID_SIZE * (ys.get(0)),
								xs.get(1) + HORIZONTAL_GRID_SIZE * (ys.get(1)),
								xs.get(2) + HORIZONTAL_GRID_SIZE * (ys.get(2)),
								xs.get(3) + HORIZONTAL_GRID_SIZE * (ys.get(3)),
								xs.get(4) + HORIZONTAL_GRID_SIZE * (ys.get(4)),
						});
						rows.add(new Row(nr,x,y,pentID,mutation));
						// System.out.println(nr + " ID" + pentID + " M" + mutation + " X" + x + " Y" +
						// y); // y?
						nr++;
					}
				}
			}
		}
		dance.algorithmX(0);
	}

	/**
	 * Checks if a pentomino piece can be placed at a given position on the board.
	 *
	 * @param piece the pentomino piece to be placed
	 * @param x     the x-coordinate of the placement position
	 * @param y     the y-coordinate of the placement position
	 * @return true if the piece can be placed, false otherwise
	 */
	public static boolean canPlacePieceBool(int[][] piece, int x, int y) {
		if (x + piece.length > HORIZONTAL_GRID_SIZE) {
			return false;
		}
		for (int i = 0; i < piece.length; i++) {
			if (y + piece[i].length > VERTICAL_GRID_SIZE)
				return false;
		}
		return true;
	}

	/**
	 * Returns the x-coordinates of occupied cells in a pentomino piece when placed
	 * on the board.
	 *
	 * @param piece the pentomino piece to be placed
	 * @param row   the row where the pentomino piece is placed
	 * @param col   the column where the pentomino piece is placed
	 * @return a list of x-coordinates of occupied cells
	 */
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

	/**
	 * Returns the y-coordinates of occupied cells in a pentomino piece when placed
	 * on the board.
	 *
	 * @param piece the pentomino piece to be placed
	 * @param row   the row where the pentomino piece is placed
	 * @param col   the column where the pentomino piece is placed
	 * @return a list of y-coordinates of occupied cells
	 */
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

	/**
	 * Draws a pentomino piece on the board.
	 *
	 * @param pentID   the ID of the pentomino piece to be drawn
	 * @param mutation the mutation of the pentomino piece
	 * @param x0       the x-coordinate of the placement position
	 * @param y0       the y-coordinate of the placement position
	 */
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
