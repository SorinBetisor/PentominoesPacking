package Phase2;

import java.util.Random;

import Phase1.PentominoDatabase;
import Phase1.Search;

public class Tetris {
    public static final int HORIZONTAL_GRID_SIZE = 5;
    public static final int VERTICAL_GRID_SIZE = 15;
    private static final char[] PIECES = { 'T', 'U', 'P', 'I', 'V', 'L','F','W','X','Y','Z','N' };
    public static int[][] field;
    public static MainScreen screen;

    public static int[][] currentPiece;
    public static int currentID;
    public static int currentX;
    public static int currentY;
    public static int currentRotation;

    public Tetris() {
        Random random = new Random();
        char randomPieceChar = PIECES[random.nextInt(13)];
        currentID = Search.characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        initializeField();
        addPiece(currentPiece, currentID, 0, 0);
        screen = new MainScreen(5, 15, 52);
    }

    // public void update() {
    //     while (true) {
    //         screen.setState(field);
    //     }
    // }

    private void moveDownByOne(int pos) {
        for (int j = 0; j < HORIZONTAL_GRID_SIZE; j++) {
            field[pos][j] = -1;
        }
        for (int i = pos - 1; i > 0; i--) {
            for (int j = 0; j < HORIZONTAL_GRID_SIZE; j++) {
                if (field[i][j] != -1) {
                    field[i + 1][j] = field[i][j];
                    field[i][j] = -1;
                }
            }
        }
    }

    public void addPiece(int[][] piece, int pentID, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0) {
                    field[i + row][j + col] = pentID;
                }
            }
        }
    }

    public void initializeField() {
        field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = -1;
            }
        }
    }

    public static void main(String[] args) {
        new Tetris();
        screen.setState(field);
    }
}
