package Phase2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Phase1.PentominoDatabase;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tetris {
    public static Random random = new Random();

    public static final int HORIZONTAL_GRID_SIZE = 5;
    public static final int VERTICAL_GRID_SIZE = 15;
    public static final int MAXIMUM_VELOCITY = 950;
    public static final int MINIMUM_VELOCITY = 150;
    public static final int INITIAL_VELOCITY = 800;

    private static final char[] PIECES = { 'T', 'U', 'P', 'I', 'V', 'L', 'F', 'W', 'X', 'Y', 'Z', 'N' };
    public static int[][] field;
    public static MainScreen screen;
    public static boolean gameOver = false;
    public static int score = 0;
    public static int highScore = 0;

    public static int[][] currentPiece;
    public static int currentID;
    public static int currentX;
    public static int currentY;
    public static int currentRotation;
    public static boolean accelerateDown = false;
    public static int pieceVelocity = INITIAL_VELOCITY;
    public static int[][] actualMatrix;

    public Tetris() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        currentX = 0;
        currentY = 0;
        initializeField();
        addPiece(currentPiece, currentID, currentX, currentY);

        if (screen == null)
        screen = new MainScreen(HORIZONTAL_GRID_SIZE, VERTICAL_GRID_SIZE, 45,
                new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE]);

    }

    /**
	 * Get as input the character representation of a pentomino and translate it
	 * into its corresponding numerical value (ID).
	 * 
	 * @param character a character representing a pentomino
	 * @return the corresponding ID (numerical value)
	 */
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

    public void startGameLoop() {
        Timer gameTimer = new Timer(pieceVelocity, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canClearRow())
                    clearRow();

                if (!moveDown()) {
                    actualMatrix = rotateMatrix(field);
                    getNextRandomPiece();

                    if (canClearRow())
                    clearRow();
                    if (checkGameOver()) {
                        System.out.println("Game Over");
                        gameOver = true;
                        if (score > highScore) {
                            highScore = score;
                        }
                        screen.showGameOver();
                        ((Timer) e.getSource()).stop(); // Stop the game timer
                    }
                }
                screen.setState(field);
            }
        });

        gameTimer.start();

        Timer updateTimerInterval = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newPieceVelocity = getUpdatedPieceVelocity(); // Replace this with your logic to get updated
                                                                  // pieceVelocity
                gameTimer.setDelay(newPieceVelocity);
            }
        });

        updateTimerInterval.start();
    }

    private static void moveRowDownByOne(int row) {
        for (int j = 0; j < actualMatrix[0].length; j++) {
            actualMatrix[row][j] = -1;
        }
        for (int i = row - 1; i > 0; i--) {
            for (int j = 0; j < actualMatrix[0].length; j++) {
                if (actualMatrix[i][j] != -1) {
                    actualMatrix[i + 1][j] = actualMatrix[i][j];
                    actualMatrix[i][j] = -1;
                }
            }
        }
        score++;

        MainScreen.updateScore();
        

        field = rotateMatrixBack(actualMatrix);
        screen.setState(field);
    }

    // PLACING AND REMOVING PIECES
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

    public static boolean checkGameOver() {
        return !canPlace(field, currentPiece, currentX, currentY);
    }

    private static void getNextRandomPiece() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        currentX = 0;
        currentY = 0;
    }

    // CLEARING ROWS
    private static boolean canClearRow() {
        for (int i = 0; i < actualMatrix.length; i++) {
            boolean canClear = true;
            for (int j = 0; j < actualMatrix[0].length; j++) {
                if (actualMatrix[i][j] == -1) {
                    canClear = false;
                }
            }
            if (canClear) {
                return true;
            }
        }
        return false;
    }

    private static void clearRow() {
        List<Integer> toClear = new ArrayList<>();
        for (int i = 0; i < actualMatrix.length; i++) {
            boolean canClear = true;
            for (int j = 0; j < actualMatrix[0].length; j++) {
                if (actualMatrix[i][j] == -1) {
                    canClear = false;
                }
            }
            if (canClear) {
                toClear.add(i);
            }
        }
        for (int i = 0; i < toClear.size(); i++) {
            moveRowDownByOne(toClear.get(i));
        }
    }

    // PIECE MOVEMENT AND ROTATION
    public static boolean moveDown() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX, currentY + 1)) {
            addPiece(currentPiece, currentID, currentX, currentY + 1);
            currentY++;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
            screen.setState(field); // Update the MainScreen
            return false;
        }
        screen.setState(field); // Update the MainScreen
        return true;
    }

    public static void dropPiece() {
        removePiece(field, currentPiece, currentX, currentY);
        while (canPlace(field, currentPiece, currentX, currentY + 1)) {
            currentY++;
        }
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field); // Update the MainScreen
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
        screen.setState(field); // Update the MainScreen
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
        screen.setState(field); // Update the MainScreen
    }

    public static void moveLeft() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX - 1, currentY)) {
            addPiece(currentPiece, currentID, currentX - 1, currentY);
            currentX--;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field); // Update the MainScreen
    }

    public static void moveRight() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX + 1, currentY)) {
            addPiece(currentPiece, currentID, currentX + 1, currentY);
            currentX++;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field); // Update the MainScreen
    }

    public static void accelerateMovingDown() {
        accelerateDown = true;
        if (pieceVelocity > MINIMUM_VELOCITY) {
            pieceVelocity -= 100;
            MainScreen.updateSpeed();
        }
    }

    public static void decelerateMovingDown() {
        accelerateDown = false;
        if (pieceVelocity < MAXIMUM_VELOCITY) {
            pieceVelocity += 100;
            MainScreen.updateSpeed();
        }
    }

    // MATRIX HELPER FUNCTIONS
    public void initializeField() {
        field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = -1;
            }
        }
        actualMatrix = rotateMatrix(field);
    }

    public static int[][] rotateMatrix(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] rotatedMatrix = new int[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rotatedMatrix[j][numRows - 1 - i] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    public static int[][] rotateMatrixBack(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] rotatedMatrix = new int[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rotatedMatrix[j][i] = matrix[i][numCols - 1 - j];
            }
        }

        return rotatedMatrix;
    }

    // RUNNING AND RESTARTING THE GAME
    public void runTetris() {
        screen.setState(field);
        startGameLoop();
    }

    public void restartTetris() {
        gameOver = false;
        score = 0;
        pieceVelocity = INITIAL_VELOCITY;
        initializeField();
        getNextRandomPiece();
        screen.setState(field);
        startGameLoop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris tetris = new Tetris();
            tetris.runTetris();
        });
    }

    // getters and setters
    private int getUpdatedPieceVelocity() {
        return pieceVelocity;
    }
}
