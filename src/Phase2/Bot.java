package Phase2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Phase2.helperClasses.Criteria;
import Phase2.helperClasses.Matrix;

// Here is the algorithm:

// For each figure rotation and for each horizontal position, simulate “spacebar” and check the height of our new tower and the number of “holes”.
// Choose the best one, and
// Press “up” key if the rotation does not correspond
// Press “left” or “right” keys to move to the corresponding position.
// Press “spacebar” if the position is correct.

public class Bot {
    public static int[][] workingField;
    private Tetris botGame;

    public Bot() {
        botGame = new Tetris();
        botGame.runTetris();
        workingField = botGame.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        Tetris.botPlaying = true;
        System.out.println(Arrays.deepToString(botGame.getWorkingField()));
        System.out.println("Bot is playing");
        while (!Tetris.gameOver) {
            // System.out.println(Criteria.calculateHeight(botGame.getWorkingField()));
            // System.out.println(Arrays.deepToString(botGame.getWorkingField()));
            // System.out.println(Criteria.calculateGaps(botGame.getWorkingField()));
            getOptimalDrop(botGame.getWorkingField(), currentPiece);
            try {
                Thread.sleep(botGame.getUpdatedPieceVelocity());
                // flush terminal
                System.out.print("\033[H\033[2J");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void getOptimalDrop(int[][] afield, int[][] currentPiece) {
        // int[][] field = Tetris.deepCopy((afield));
        // List<int[][]> rotations = new ArrayList<>();
        // rotations.add(currentPiece);
        // rotations.add(rotatePiece90(currentPiece));
        // rotations.add(rotatePiece90(rotatePiece90(currentPiece)));
        // rotations.add(rotatePiece90(rotatePiece90(rotatePiece90(currentPiece))));

        List<Map<String, Object>> drops = new ArrayList<>();
        for (int rotation = 0; rotation <= 3; rotation++) { // rotations.size()
            final int currentRotation = rotation;
            for (int xposition = 0; xposition < (5 - (currentPiece[0].length))+1; xposition++) {
                final int currentXPos = xposition;
                int[][] simulatedBoard = Matrix.rotateMatrix(botGame.getSimulatedDropField());
                drops.add(new HashMap<String, Object>() {
                    {
                        put("rotation", currentRotation);
                        put("xpos", currentXPos);
                        put("simulatedBoard", simulatedBoard);
                        put("canClear", Criteria.canClearRow(simulatedBoard));
                        put("height", Criteria.calculateHeight(simulatedBoard));
                        put("gaps", Criteria.calculateGaps(simulatedBoard));
                    }
                });
                Tetris.moveRight();
            }
            Tetris.moveLeftToTheBorder();

            Tetris.rotateRight();
            // Tetris.currentPiece = piece;
            // try {
            // Thread.sleep(botGame.getUpdatedPieceVelocity());
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            //flush the terminal
            System.out.print("\033[H\033[2J");
            System.out.println(drops);
        }
    }

    /**
     * Simulates dropping the current piece in a simulated board.
     *
     * @return The array with the piece dropped.
     */
    public static int[][] simulateDrop(int[][] piece) {
        int[][] simulatedField = Matrix.deepCopy(Tetris.field);
        int simulatedX = Tetris.currentX;
        int simulatedY = Tetris.currentY;

        while (Tetris.canPlace(simulatedField, piece, simulatedX, simulatedY + 1)) {
            simulatedY++;
        }

        // Add the piece to the simulated field
        Tetris.addPiece(simulatedField, piece, Tetris.currentID, simulatedX, simulatedY);
        return simulatedField;
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        bot.runBot(Tetris.field, Tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }

    // Helper Functions

    public static int[][] rotatePiece90(int[][] piece) {
        int[][] rotatedPiece = new int[piece[0].length][piece.length];
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                rotatedPiece[j][piece.length - 1 - i] = piece[i][j];
            }
        }
        return rotatedPiece;
    }

}