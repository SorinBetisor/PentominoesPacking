package Phase2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Phase1.PentominoDatabase;
import Phase2.helperClasses.Matrix;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Tetris class represents the game of Tetris.
 * It contains methods for placing and removing pieces, clearing rows, and
 * moving and rotating pieces.
 * It also has constants for the size of the game grid and the maximum and
 * minimum velocities of the falling pieces.
 * The class uses a MainScreen object to display the game to the user.
 */

public class Tetris {
    public static Random random = new Random();
    public static final int HORIZONTAL_GRID_SIZE = 5;
    public static final int VERTICAL_GRID_SIZE = 15;
    public static final int MAXIMUM_VELOCITY = 950;
    public static final int MINIMUM_VELOCITY = 150;
    public static final int INITIAL_VELOCITY = 800;
    private static final char[] PIECES = { 'T', 'U', 'P', 'I', 'V', 'L', 'F', 'W', 'X', 'Y', 'Z', 'N' };

    public int[][] field;
    public int[][] fieldWithoutCurrentPiece;
    public int[][] simulatedDropField;
    public MainScreen screen;
    public boolean gameOver = false;
    public boolean botPlaying = false;
    public int score = 0;
    public int highScore = 0;

    public int[][] currentPiece;
    public int currentID;
    public int currentX;
    public int currentY;
    public int currentLowestY;
    public int currentRotation;
    public boolean accelerateDown = false;
    public int pieceVelocity = INITIAL_VELOCITY;
    public int[][] actualMatrix;

    /**
     * Constructs a new Tetris game instance with a randomly selected piece,
     * initializes the game field, and creates a new MainScreen object if one does
     * not already exist.
     */
    public Tetris() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        currentX = 0;
        currentY = 0;
        initializeField();
        fieldWithoutCurrentPiece = Matrix.rotateMatrix(field).clone();
        addPiece(currentPiece, currentID, currentX, currentY);

        if (screen == null)
            screen = new MainScreen(HORIZONTAL_GRID_SIZE, VERTICAL_GRID_SIZE, 45,
                    new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE], this);

    }

    // RUNNING AND RESTARTING THE GAME
    /**
     * Sets the state of the screen to the current field and starts the game loop.
     */
    public void runTetris() {
        screen.setState(field);
        startGameLoop();
    }

    /**
     * Resets the game state and starts a new game of Tetris.
     * Sets the game over flag to false, score to 0, piece velocity to initial
     * velocity,
     * initializes the game field, gets the next random piece, sets the screen state
     * to the field,
     * and starts the game loop.
     */
    public void restartTetris() {
        gameOver = false;
        score = 0;
        pieceVelocity = INITIAL_VELOCITY;
        initializeField();
        getNextRandomPiece();
        screen.setState(field);
        startGameLoop();
    }

    /**
     * Starts the game loop which updates the game state and screen at a fixed
     * interval.
     * Uses two timers, one for the game loop and another for updating the piece
     * velocity.
     */
    public void startGameLoop() {
        Timer gameTimer = new Timer(pieceVelocity, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (canClearRow())
                    clearRow();
                    if (!moveDown()) {
                    actualMatrix = Matrix.rotateMatrix(field);
                    fieldWithoutCurrentPiece = Matrix.deepCopy(Matrix.rotateMatrix(field));
                    getNextRandomPiece();
                    // simulatedDropField = Bot.simulateDrop(currentPiece);

                    if (canClearRow()) {
                        clearRow();
                        // simulatedDropField = Bot.simulateDrop(currentPiece);
                    } else {
                        // simulatedDropField = Bot.simulateDrop(currentPiece);
                    }
                    if (checkGameOver()) {
                        System.out.println("Game Over");
                        gameOver = true;
                        if (score > highScore) {
                            highScore = score;
                        }
                        screen.showGameOver();
                        ((Timer) e.getSource()).stop();
                    }
                }
                screen.setState(field);
            }
        });

        gameTimer.start();

        Timer updateTimerInterval = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newPieceVelocity = getUpdatedPieceVelocity();
                gameTimer.setDelay(newPieceVelocity);
            }
        });

        updateTimerInterval.start();
    }

    /**
     * Moves the specified row down by one and updates the score.
     * 
     * @param row the row to move down
     */
    private void moveRowDownByOne(int row) {
        for (int j = 0; j < this.actualMatrix[0].length; j++) {
            this.actualMatrix[row][j] = -1;
        }
        for (int i = row - 1; i > 0; i--) {
            for (int j = 0; j < this.actualMatrix[0].length; j++) {
                if (this.actualMatrix[i][j] != -1) {
                    this.actualMatrix[i + 1][j] = this.actualMatrix[i][j];
                    this.actualMatrix[i][j] = -1;
                }
            }
        }
        score++;

        MainScreen.updateScore();

        field = Matrix.rotateMatrixBack(actualMatrix);
        screen.setState(field);
    }

    // PLACING AND REMOVING PIECES
    /**
     * Determines if a given piece can be placed on the field at the specified row
     * and column.
     * 
     * @param field the field to place the piece on
     * @param piece the piece to place on the field
     * @param row   the row to place the piece at
     * @param col   the column to place the piece at
     * @return true if the piece can be placed at the specified location, false
     *         otherwise
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

    /**
     * Removes a piece from the field at the specified row and column.
     * 
     * @param field the field to remove the piece from
     * @param piece the piece to remove
     * @param row   the row to remove the piece from
     * @param col   the column to remove the piece from
     */
    public void removePiece(int[][] field, int[][] piece, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0) {
                    field[i + row][j + col] = -1;
                }
            }
        }
    }

    /**
     * Adds a given piece to the game field at the specified row and column.
     * 
     * @param piece  the piece to be added to the field
     * @param pentID the ID of the piece
     * @param row    the row where the piece will be added
     * @param col    the column where the piece will be added
     */
    public void addPiece(int[][] piece, int pentID, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0) {
                    field[i + row][j + col] = pentID;
                }
            }
        }
    }

    /**
     * OVERLOAD
     * Adds a piece to the board at the specified position.
     * 
     * @param board  the game board represented as a 2D array
     * @param piece  the piece to be added represented as a 2D array
     * @param pentID the ID of the piece
     * @param row    the row index where the piece should be added
     * @param col    the column index where the piece should be added
     */
    public void addPiece(int[][] board, int[][] piece, int pentID, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != 0) {
                    board[i + row][j + col] = pentID;
                }
            }
        }
    }

    /**
     * This method selects a random piece from the PIECES array and sets the current
     * piece, ID, and position accordingly.
     * If the randomly selected piece is one of the pieces that can be flipped,
     * there is a 50% chance that the piece will be flipped.
     */
    private void getNextRandomPiece() {
        char randomPieceChar = PIECES[random.nextInt(12)];
        currentID = characterToID(randomPieceChar);
        currentPiece = PentominoDatabase.data[currentID][0];
        if (random.nextInt(2) == 1) {
            if (randomPieceChar == 'F' || randomPieceChar == 'L' || randomPieceChar == 'P' || randomPieceChar == 'Z'
                    || randomPieceChar == 'T' || randomPieceChar == 'Y') {
                currentPiece = flipPiece();
            }
        }
        currentX = 0;
        currentY = 0;
    }

    // CLEARING ROWS
    private boolean canClearRow() {
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

    /**
     * Clears any row in the actualMatrix that is completely filled with blocks.
     * It moves all the rows above the cleared row down by one.
     */
    private void clearRow() {
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
    /**
     * Moves the current piece down by one row if possible, updates the game field
     * and the MainScreen.
     * If the piece cannot be moved down, it is added to the game field and the
     * method returns false.
     * 
     * @return true if the piece was successfully moved down, false otherwise.
     */
    public boolean moveDown() {
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

    /**
     * Drops the current piece down as far as it can go and updates the game field.
     */
    public void dropPiece() {
        removePiece(field, currentPiece, currentX, currentY);
        while (canPlace(field, currentPiece, currentX, currentY + 1)) {
            currentY++;
        }
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field); // Update the MainScreen
    }

    /**
     * Rotates the current piece 90 degrees clockwise and updates the game field.
     * If the rotated piece can be placed in the current position, it becomes the
     * new current piece.
     * Otherwise, the original piece remains in place.
     */
    public void rotateRight() {
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

    /**
     * Flips the current piece horizontally.
     * 
     * @return the flipped piece as a 2D integer array.
     */
    public int[][] flipPiece() {
        int[][] flippedPiece = new int[currentPiece.length][currentPiece[0].length];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[0].length; j++) {
                flippedPiece[i][j] = currentPiece[i][currentPiece[0].length - 1 - j];
            }
        }
        return flippedPiece;
    }

    /**
     * Rotates the current piece to the left.
     * Removes the current piece from the field, rotates it, and checks if it can be
     * placed in the new position.
     * If it can be placed, updates the current piece with the rotated piece and
     * adds it to the field.
     * Finally, updates the MainScreen with the new state of the field.
     */
    public void rotateLeft() {
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

    /**
     * Moves the current piece one cell to the left on the game field, if possible.
     * If the piece cannot be moved, it remains in its current position.
     * Updates the MainScreen with the new state of the game field.
     */
    public void moveLeft() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX - 1, currentY)) {
            addPiece(currentPiece, currentID, currentX - 1, currentY);
            currentX--;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field); // Update the MainScreen
    }

    public void moveLeftToTheBorder()
    {
        removePiece(field, currentPiece, currentX, currentY);
        while (canPlace(field, currentPiece, currentX - 1, currentY)) {
            currentX--;
        }
        addPiece(currentPiece, currentID, currentX, currentY);
        screen.setState(field); // Update the MainScreen
    }

    /**
     * Moves the current piece one cell to the right on the game field, if possible.
     * If the piece cannot be moved, it remains in its current position.
     * Updates the MainScreen with the new state of the game field.
     */
    public void moveRight() {
        removePiece(field, currentPiece, currentX, currentY);
        if (canPlace(field, currentPiece, currentX + 1, currentY)) {
            addPiece(currentPiece, currentID, currentX + 1, currentY);
            currentX++;
        } else {
            addPiece(currentPiece, currentID, currentX, currentY);
        }
        screen.setState(field); // Update the MainScreen
    }

    /**
     * Sets the accelerateDown flag to true and decreases the pieceVelocity by 100
     * if it is greater than the minimum velocity.
     * Calls the updateSpeed method of the MainScreen class to update the speed of
     * the game.
     */
    public void accelerateMovingDown() {
        accelerateDown = true;
        if (pieceVelocity > MINIMUM_VELOCITY) {
            pieceVelocity -= 100;
            screen.updateSpeed();
        }
    }

    /**
     * Decelerates the falling speed of the tetromino piece.
     * Sets accelerateDown to false and increases the pieceVelocity by 100 if it is
     * less than MAXIMUM_VELOCITY.
     * Updates the speed on the MainScreen.
     */
    public void decelerateMovingDown() {
        accelerateDown = false;
        if (pieceVelocity < MAXIMUM_VELOCITY) {
            pieceVelocity += 100;
            screen.updateSpeed();
        }
    }

    // MATRIX HELPER FUNCTIONS
    /**
     * Initializes the field by creating a new 2D integer array with the dimensions
     * of HORIZONTAL_GRID_SIZE and VERTICAL_GRID_SIZE.
     * Then, it sets all the values of the array to -1.
     * Finally, it rotates the matrix and sets it as the actual matrix.
     */
    public void initializeField() {
        field = new int[HORIZONTAL_GRID_SIZE][VERTICAL_GRID_SIZE];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = -1;
            }
        }
        actualMatrix = Matrix.rotateMatrix(field);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris tetris = new Tetris();
            tetris.runTetris();
        });
    }

    // getters and setters
    public int getUpdatedPieceVelocity() {
        return pieceVelocity;
    }

    public int[][] getWorkingField() {
        return fieldWithoutCurrentPiece;
    }

    public int[][] getSimulatedDropField() {
        // Create a deep copy of the current field
        int[][] copy = Matrix.deepCopy(field);
        
        // Remove the current piece from the copy
        removePiece(copy, currentPiece, currentX, currentY);
        
        // Find the lowest position where the current piece can be placed
        int lowestY = 0;
        while (canPlace(copy, currentPiece, currentX, lowestY + 1)) {
            if (lowestY + 1 > VERTICAL_GRID_SIZE - currentPiece.length) {
                lowestY++;
                break;
            }
            lowestY++;
        }
    
        // Simulate dropping the piece to the lowest position
        int[][] simulatedDropField = Matrix.deepCopy(copy);
        addPiece(simulatedDropField, currentPiece, currentID, currentX, lowestY);
    
        return simulatedDropField;
    }

    public int getLowestY() {
        int[][] copy = Matrix.deepCopy(field);
        removePiece(copy, currentPiece, currentX, currentY);
        int lowestY = 0;
        while (canPlace(copy, currentPiece, currentX, lowestY + 1)) {
            if (lowestY + 1 > VERTICAL_GRID_SIZE - currentPiece.length) {
                lowestY++;
                break;
            }
            lowestY++;
        }
        return 15 - lowestY;
    }

    /**
     * Checks if the game is over by determining if the current piece can be placed
     * on the field.
     * 
     * @return true if the game is over, false otherwise.
     */
    public boolean checkGameOver() {
        return !canPlace(field, currentPiece, currentX, currentY);
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
}
