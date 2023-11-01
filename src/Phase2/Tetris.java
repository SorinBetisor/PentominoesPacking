package Phase2;

import java.util.Random;
import Phase1.PentominoDatabase;
import Phase1.Search;

public class Tetris {
    public static Random random = new Random();

    //TODO: GENERATE JAVADOC COMMENTS FOR THE WHOLE DOCUMENT

    public static final int HORIZONTAL_GRID_SIZE = 5;
    public static final int VERTICAL_GRID_SIZE = 15;
    public static final int MAXIMUM_VELOCITY = 950;
    public static final int MINIMUM_VELOCITY = 150;
    public static final int INITIAL_VELOCITY = 800;

    private static final char[] PIECES = { 'T', 'U', 'P', 'I', 'V', 'L', 'F', 'W', 'X', 'Y', 'Z', 'N' };
    public static int[][] field;
    public static MainScreen screen;
    public static boolean gameOver = false;

    public static int[][] currentPiece;
    public static int currentID;
    public static int currentX;
    public static int currentY;
    public static int currentRotation;
    public static boolean accelerateDown = false;
    public static int pieceVelocity = INITIAL_VELOCITY;

    public Tetris() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = Search.characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        currentX = 0; // Initialize currentX and currentY here
        currentY = 0;
        initializeField();
        addPiece(currentPiece, currentID, currentX, currentY); // Add the piece after initialization
        screen = new MainScreen(5, 15, 45, field);
    }

    /*
     * private void moveRowDownByOne(int pos) {
     * for (int j = 0; j < HORIZONTAL_GRID_SIZE; j++) {
     * field[pos][j] = -1;
     * }
     * for (int i = pos - 1; i > 0; i--) {
     * for (int j = 0; j < HORIZONTAL_GRID_SIZE; j++) {
     * if (field[i][j] != -1) {
     * field[i + 1][j] = field[i][j];
     * field[i][j] = -1;
     * }
     * }
     * }
     * }
     */

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

    /* Movement and rotation */
    public static boolean moveDown() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX, currentY + 1)) {
            addPiece(currentPiece, currentID, currentX, currentY + 1);
            currentY++;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
            screen.setState(field);
            return false;
        }
        screen.setState(field);
        return true;
    }

    public static void rotateRight() {
        removePiece(field, currentPiece, currentX, currentY);
        int[][] rotatedPiece = new int[currentPiece[0].length][currentPiece.length];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[0].length; j++) {
                rotatedPiece[j][currentPiece.length - 1 - i] = currentPiece[i][j];
            }
        }
        if (canPlace(field, rotatedPiece, currentX, currentY)) {
            currentPiece = rotatedPiece;
        }
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field);
    }

    public static void rotateLeft() {
        removePiece(field, currentPiece, currentX, currentY);
        int[][] rotatedPiece = new int[currentPiece[0].length][currentPiece.length];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[0].length; j++) {
                rotatedPiece[currentPiece[0].length - 1 - j][i] = currentPiece[i][j];
            }
        }
        if (canPlace(field, rotatedPiece, currentX, currentY)) {
            currentPiece = rotatedPiece;
        }
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field);
    }

    public static void moveLeft() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX - 1, currentY)) {
            addPiece(currentPiece, currentID, currentX - 1, currentY);
            currentX--;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field);
    }

    public static void moveRight() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX + 1, currentY)) {
            addPiece(currentPiece, currentID, currentX + 1, currentY);
            currentX++;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field);
    }

    public static void accelerateMovingDown() {
        accelerateDown = true;
        if(pieceVelocity > MINIMUM_VELOCITY)
        pieceVelocity -= 100;
    }

    public static void decelerateMovingDown() {
        accelerateDown = false;
        if(pieceVelocity < MAXIMUM_VELOCITY)
        pieceVelocity += 100;
    }

    public static boolean checkGameOver() {
        return !canPlace(field, currentPiece, currentX, currentY);
    }

    private static void getNextRandomPiece() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = Search.characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        currentX = 0;
        currentY = 0;
    }

    public static void update() {
        while (true) {
            try {
                Thread.sleep(pieceVelocity); // Use pieceVelocity to control the sleep time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!moveDown()) {
                getNextRandomPiece();

                if (checkGameOver()) {
                    System.out.println("Game Over");
                    gameOver = true;
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Tetris();
        screen.setState(field);
        update();
    }
}
