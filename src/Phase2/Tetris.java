package Phase2;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import Phase1.PentominoDatabase;
import Phase1.Search;

public class Tetris {
    public static final int HORIZONTAL_GRID_SIZE = 5;
    public static final int VERTICAL_GRID_SIZE = 15;
    private static final char[] PIECES = { 'T', 'U', 'P', 'I', 'V', 'L', 'F', 'W', 'X', 'Y', 'Z', 'N' };
    public static int[][] field;
    public static MainScreen screen;

    public static int[][] currentPiece;
    public static int currentID;
    public static int currentX;
    public static int currentY;
    public static int currentRotation;
    private static Timer timer;
    public Tetris() {
        Random random = new Random();
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = Search.characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        initializeField();
        currentX = HORIZONTAL_GRID_SIZE / 2 - 1;
        addPiece(currentPiece, currentID, currentX, currentY);
        screen = new MainScreen(5, 15, 45);
    }
    

    public static void update() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveDown();
            }
        }, 0, 500);
    }

   /*  private void moveRowDownByOne(int pos) {
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
*/ 
    public static boolean moveDown() {
    removePiece(field, currentPiece, currentX, currentY);
    if (canPlace(field, currentPiece, currentX, currentY + 1)) {
        addPiece(currentPiece, currentID, currentX, currentY + 1);
        currentY++;
    } else {
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field);
        timer.cancel(); // Stop the timer when the piece cannot move down
        return false;
    }
    screen.setState(field);
    return true;
}


    public static boolean canPlace(int[][] field, int[][] piece, int row, int col) {
        if (row < 0 || col < 0 || row + piece.length > field.length || col + piece[0].length > field[0].length) {
            return false;
        }

        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0 && field[i + row][j + col] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void removePiece(int[][] field, int[][] piece, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0) {
                    field[i + row][j + col] = -1;
                }
            }
        }
    }

    public static void addPiece(int[][] piece, int pentID, int row, int col) {
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

        update();
    }
}
