package Phase3.Solvers.DancingLinks;

import java.util.ArrayList;
import java.util.List;
import Phase3.PiecesDB.ParcelDB;
import Phase3.PiecesDB.PentominoesDB;
import Phase3.Solvers.SearchWrapper;
import Phase3.Visualizer.FXVisualizer;

/**
 * The DLX3D class represents a solver for a 3D placement problem using Dancing
 * Links algorithm.
 * It provides methods to create positions, check if a piece is placeable, and
 * get occupied cells.
 * The solver supports two types of pieces: Pentominoes and Parcels.
 */
public class DLX3D {

    private int depth = FXVisualizer.CARGO_DEPTH;
    private int height = FXVisualizer.CARGO_HEIGHT;
    private int width = FXVisualizer.CARGO_WIDTH;
    public static int totalValue = 0;
    public static int[][][][] A;
    public static int[][][][] B;
    public static int[][][][] C;
    public static int[][][][][] shapes;
    public int[] values;
    private DancingLinks2 dance;
    public static List<Row> rows = new ArrayList<Row>();
    public static int value = 0;
    public static boolean pent = false;
    public static int limit = 0;
    public static int pieceCount = 0;

    /**
     * Constructs a new DLX3D object with the specified type of pieces and values.
     * 
     * @param typeOfPieces the type of pieces ("Pentominoes" or "Parcels")
     * @param newValues    an array of Piece values
     */
    public DLX3D(String typeOfPieces, int[] newValues, boolean a, boolean b, boolean c) {
        refreshDLX();
        if (typeOfPieces.equals("Pentominoes")) {
            A = PentominoesDB.lPentInt;
            B = PentominoesDB.pPentInt;
            C = PentominoesDB.tPentInt;
            pent = true;
        } else if (typeOfPieces.equals("Parcels")) // only for testing purposes, not usable with the GUI
        {
            A = ParcelDB.aRotInt;
            B = ParcelDB.bRotInt;
            C = ParcelDB.cRotInt;
            pent = false;
        }

        if (!a && b && c) {
            A = new int[5][5][5][5];
            values = new int[] { newValues[2], newValues[1], newValues[1] };
            shapes = new int[][][][][] { C,B,A };
        }
        else if(a && !b && c) {
            B = new int[5][5][5][5];
            SearchWrapper.invertArray(newValues);
            values = newValues;
            shapes = new int[][][][][] { C,B,A};}
        else if(a && b && !c) {
            C = new int[5][5][5][5];
            SearchWrapper.invertArray(newValues);
            values = newValues;
            shapes = new int[][][][][] { C,B,A};}
         else {//1044
            shapes = new int[][][][][] { C, A, B };
            SearchWrapper.invertArray(newValues);
            values = newValues;
        }
        dance = new DancingLinks2(depth * height * width);
    }

    /**
     * Refreshes the Dancing Links solver by resetting the necessary variables.
     */
    public static void refreshDLX() {
        rows = new ArrayList<Row>();
        value = 0;
        totalValue = 0;
        pieceCount = 0;
    }

    /**
     * Creates positions for the shapes in the Dancing Links solver.
     * This method iterates over the shapes and their placements to determine valid
     * positions.
     * It populates the rows list and adds rows to the dance object.
     * Finally, it invokes the algorithmX method of the dance object.
     */
    public void createPositions() {
        int currentPieceValue = 0;
        int typeNumber = 1;
        int nr = 0;
        for (int[][][][] typeOfShape : shapes) {
            int shapeWidth = typeOfShape[0][0][0].length;
            int shapeHeight = typeOfShape[0][0].length;
            int shapeDepth = typeOfShape[0].length;
            limit = 0;
            if (pent) {
                limit = 5;
            } else {
                limit = shapeDepth * shapeHeight * shapeWidth;
            }
            currentPieceValue = values[typeNumber - 1];
            for (int[][][] shape : typeOfShape) {
                for (int zPlacementStart = 0; zPlacementStart < depth; zPlacementStart++) {
                    for (int yPlacementStart = 0; yPlacementStart < height; yPlacementStart++) {
                        for (int xPlacementStart = 0; xPlacementStart < width; xPlacementStart++) {
                            boolean can = isPlaceable(xPlacementStart, yPlacementStart, zPlacementStart, shape);
                            if (!can) {
                                continue;
                            }
                            List<Integer> xs = getOccupiedCellsX(shape, xPlacementStart, yPlacementStart,
                                    zPlacementStart);
                            List<Integer> ys = getOccupiedCellsY(shape, xPlacementStart, yPlacementStart,
                                    zPlacementStart);
                            List<Integer> zs = getOccupiedCellsZ(shape, xPlacementStart, yPlacementStart,
                                    zPlacementStart);

                            int[] dobavkaFinal = new int[limit];
                            int[] dobavkaX = new int[xs.size()];
                            int[] dobavkaY = new int[ys.size()];
                            int[] dobavkaZ = new int[zs.size()];
                            for (int i = 0; i < xs.size(); i++) {
                                dobavkaX[i] = xs.get(i);
                            }
                            for (int i = 0; i < ys.size(); i++) {
                                dobavkaY[i] = ys.get(i);
                            }
                            for (int i = 0; i < zs.size(); i++) {
                                dobavkaZ[i] = zs.get(i);
                            }
                            for (int i = 0; i < dobavkaX.length; i++) {
                                dobavkaFinal[i] = depth * height * dobavkaX[i] + depth * dobavkaY[i] + dobavkaZ[i];
                            }
                            rows.add(new Row(nr, xPlacementStart, yPlacementStart, zPlacementStart, typeNumber, shape,
                                    currentPieceValue));
                            dance.AddRow(nr, typeNumber, dobavkaFinal, shape);
                            nr++;
                        }
                    }
                }
            }
            typeNumber++;
        }
        dance.algorithmX(0);
    }

    /**
     * Returns a list of occupied cells in the x-axis for a given piece to place.
     * 
     * @param pieceToPlace the 3D array representing the piece to place
     * @param x0           the starting x-coordinate
     * @param y0           the starting y-coordinate
     * @param z0           the starting z-coordinate
     * @return a list of occupied cells in the x-axis
     */
    public List<Integer> getOccupiedCellsX(int[][][] pieceToPlace, int x0, int y0, int z0) {
        List<Integer> xs = new ArrayList<Integer>();
        int distanceX = -1;
        for (int z1 = 0; z1 < pieceToPlace.length; z1++) {
            for (int y1 = 0; y1 < pieceToPlace[z1].length; y1++) {
                for (int x1 = 0; x1 < pieceToPlace[z1][y1].length; x1++) {
                    if (pieceToPlace[z1][y1][x1] != 0 && x1 != 0) {
                        distanceX = x1;
                        break;
                    }
                }
                if (distanceX != -1) {
                    break;
                }
            }
            if (distanceX != -1) {
                break;
            }
        }
        for (int z = 0; z < pieceToPlace.length; z++) {
            for (int y = 0; y < pieceToPlace[z].length; y++) {
                for (int x = 0; x < pieceToPlace[z][y].length; x++) {
                    if (pieceToPlace[z][y][x] != 0) {
                        xs.add(x + x0);
                    }
                }
            }
        }
        return xs;

    }

    /**
     * Returns a list of occupied cell positions in the Y-axis for a given piece to
     * place.
     * 
     * @param pieceToPlace The 3D array representing the piece to be placed.
     * @param x0           The starting X-coordinate of the piece.
     * @param y0           The starting Y-coordinate of the piece.
     * @param z0           The starting Z-coordinate of the piece.
     * @return A list of Y-coordinates of the occupied cells.
     */
    public List<Integer> getOccupiedCellsY(int[][][] pieceToPlace, int x0, int y0, int z0) {
        List<Integer> ys = new ArrayList<Integer>();
        int distanceY = -1;
        for (int z = 0; z < pieceToPlace.length; z++) {
            for (int y = 0; y < pieceToPlace[z].length; y++) {
                for (int x = 0; x < pieceToPlace[z][y].length; x++) {
                    if (pieceToPlace[z][y][x] != 0 && x != 0) {
                        distanceY = y;
                        break;
                    }
                }
                if (distanceY != -1) {
                    break;
                }
            }
            if (distanceY != -1) {
                break;
            }
        }
        for (int z = 0; z < pieceToPlace.length; z++) {
            for (int y = 0; y < pieceToPlace[z].length; y++) {
                for (int x = 0; x < pieceToPlace[z][y].length; x++) {
                    if (pieceToPlace[z][y][x] != 0) {
                        ys.add(y + y0);
                    }
                }
            }
        }
        return ys;
    }

    /**
     * Returns a list of occupied cells in the Z-axis for a given piece to place.
     * 
     * @param pieceToPlace the 3D array representing the piece to place
     * @param x0           the starting x-coordinate
     * @param y0           the starting y-coordinate
     * @param z0           the starting z-coordinate
     * @return a list of occupied cells in the Z-axis
     */
    public List<Integer> getOccupiedCellsZ(int[][][] pieceToPlace, int x0, int y0, int z0) {
        List<Integer> zs = new ArrayList<Integer>();
        int distanceZ = -1;
        for (int z = 0; z < pieceToPlace.length; z++) {
            for (int y = 0; y < pieceToPlace[z].length; y++) {
                for (int x = 0; x < pieceToPlace[z][y].length; x++) {
                    if (pieceToPlace[z][y][x] != 0 && x != 0) {
                        distanceZ = z;
                        break;
                    }
                }
                if (distanceZ != -1) {
                    break;
                }
            }
            if (distanceZ != -1) {
                break;
            }
        }

        for (int i = 0; i < pieceToPlace.length; i++) {
            for (int j = 0; j < pieceToPlace[i].length; j++) {
                for (int k = 0; k < pieceToPlace[i][j].length; k++) {
                    if (pieceToPlace[i][j][k] != 0) {
                        zs.add(i + z0);
                    }
                }
            }
        }
        return zs;
    }

    /**
     * Checks if a given shape can be placed at the specified starting position in a
     * 3D grid.
     * 
     * @param startX the x-coordinate of the starting position
     * @param startY the y-coordinate of the starting position
     * @param startZ the z-coordinate of the starting position
     * @param shape  the shape to be placed in the grid
     * @return true if the shape can be placed, false otherwise
     */
    public boolean isPlaceable(int startX, int startY, int startZ, int[][][] shape) {

        int shapeWidth = shape[0][0].length;
        int shapeHeight = shape[0].length;
        int shapeDepth = shape.length;

        if (startX + shapeWidth > width) {
            return false;
        }

        if (startY + shapeHeight > height) {
            return false;
        }

        return startZ + shapeDepth <= depth;
    }

    /**
     * The main method is the entry point of the program.
     * It creates an instance of the DLX3D class and calls the createPositions
     * method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DLX3D dlx = new DLX3D("Pentominoes", new int[] { 3, 4, 5 }, true, true, true);
        dlx.createPositions();

    }

}
