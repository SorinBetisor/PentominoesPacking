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
    private Tetris tetris;

    public Bot() {
        tetris = new Tetris();
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        tetris.botPlaying = true;
        System.out.println(Arrays.deepToString(tetris.getWorkingField()));
        System.out.println("Bot is playing");
        while (!tetris.gameOver) {
            getAllPossibleDrops(tetris.getWorkingField(), currentPiece);
            try {
                Thread.sleep(tetris.getUpdatedPieceVelocity());
                // flush terminal
                System.out.print("\033[H\033[2J");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //TODO: Select most optimal drop
    public void getAllPossibleDrops(int[][] afield, int[][] currentPiece) {

        List<Map<String, Object>> drops = new ArrayList<>();
        for (int rotation = 0; rotation <= 3; rotation++) { // rotations.size()
            final int currentRotation = rotation;
            for (int xposition = 0; xposition <= (5 - (currentPiece[0].length)); xposition++) {
                final int currentXPos = xposition;
                int[][] simulatedBoard = Matrix.rotateMatrix(tetris.getSimulatedDropField());
                drops.add(new HashMap<String, Object>() {
                    {
                        put("rotation", currentRotation);
                        put("xpos", currentXPos);
                        // put("simulatedBoard", simulatedBoard);
                        put("canClear", Criteria.canClearRow(simulatedBoard));
                        put("height", Criteria.calculateHeight(simulatedBoard));
                        put("gaps", Criteria.calculateGaps(simulatedBoard));
                    }
                });
                tetris.moveRight();
            }
            tetris.moveLeftToTheBorder();
            tetris.rotateRight();
            System.out.print("\033[H\033[2J");
            System.out.println(drops);
        }
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        Tetris tetris = bot.tetris; // Access the Tetris instance
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }

    // Helper Functions

}