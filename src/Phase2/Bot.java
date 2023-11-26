package Phase2;

import java.util.ArrayList;
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

    private int currentBestRotation;
    private int currentBestXPos;

    public Bot() {
        tetris = new Tetris();
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        tetris.botPlaying = true;
        // System.out.println(Arrays.deepToString(tetris.getWorkingField()));
        System.out.println("Bot is playing");
        while (!tetris.gameOver) {
            getAllPossibleDrops(tetris.getWorkingField(), currentPiece);
            try {
                Thread.sleep(tetris.getUpdatedPieceVelocity() * 3);
                // flush terminal
                // System.out.print("\033[H\033[2J");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void getAllPossibleDrops(int[][] afield, int[][] currentPiece) {

        List<Map<String, Object>> drops = new ArrayList<>();
        tetris.moveLeftToTheBorder();
        for (int rotation = 0; rotation <= 3; rotation++) { // rotations.size()
            final int currentRotation = rotation;
            tetris.currentRotation = rotation;
            for (int xposition = 0; xposition <= (Tetris.HORIZONTAL_GRID_SIZE
                    - (currentPiece[0].length)); xposition++) {
                final int currentXPos = tetris.currentX;
                int[][] simulatedBoard = Matrix.rotateMatrix(tetris.getSimulatedDropField());
                drops.add(new HashMap<String, Object>() {
                    {
                        put("rotation", currentRotation);
                        put("xpos", currentXPos);
                        // put("simulatedBoard", simulatedBoard);
                        put("canClear", Criteria.calculateClearRows(simulatedBoard));
                        put("height", Criteria.calculateHeight(simulatedBoard));
                        put("gaps", Criteria.calculateGaps(simulatedBoard));
                        put("bumpiness", Criteria.calculateBumpiness());
                        put("floorTouchingBlocks", Criteria.calculateFloorTouchingBlocks(simulatedBoard));
                        put("wallTouchingBlocks", Criteria.calculateWallTouchingBlocks(simulatedBoard));
                        put("edgesTouchingBlocks", Criteria.calculateEdgesTouchingBlocks(simulatedBoard));
                    }
                });
                tetris.moveRight();
            }
            tetris.rotateRight();
            // System.out.print("\033[H\033[2J");
            System.out.println(drops);
        }
        int bestMoveIndex = calculateBestMove(drops);
        tetris.rotateLeft();
        tetris.rotateLeft();

        tetris.rotateLeft();

        if (bestMoveIndex != -1) {
            performBestDrop();
        }
    }

    // write a function that accesses the drops hashmap and calculates the minimum
    // sum of the criterias
    public int calculateBestMove(List<Map<String, Object>> drops) {
        double maxScore = Integer.MIN_VALUE;
        int bestMoveIndex = -1;

        for (int i = 0; i < drops.size(); i++) {
            Map<String, Object> drop = drops.get(i);
            double score = calculateScore(drop);

            if (score > maxScore) {
                maxScore = score;
                bestMoveIndex = i;

                currentBestRotation = (int) drop.get("rotation");
                currentBestXPos = (int) drop.get("xpos");
            }
        }
        System.out.println("Best move index: " + bestMoveIndex);
        System.out.println("Best move score: " + maxScore);
        System.out.println("Current best rotation: " + currentBestRotation);
        System.out.println("Current best x position: " + currentBestXPos);
        return bestMoveIndex;
    }

    public void performBestDrop() {

        tetris.moveLeftToTheBorder();
        tetris.removePiece(tetris.field, tetris.currentPiece, tetris.currentX, tetris.currentY);
        while (tetris.currentX < currentBestXPos) {
            tetris.moveRight();
        }
        if (tetris.currentRotation < currentBestRotation) {
            // System.out.println("rotating right");

            while (tetris.currentRotation != currentBestRotation) {
                tetris.rotateRight();
            }
        } else if (tetris.currentRotation > currentBestRotation) {
            // System.out.println("rotating left");

            while (tetris.currentRotation != currentBestRotation) {
                tetris.rotateLeft();
            }
        }

        tetris.currentX = currentBestXPos;
        tetris.currentRotation = currentBestRotation;
        tetris.addPiece(tetris.currentPiece, tetris.currentID, tetris.currentX, tetris.currentY);

        tetris.dropPiece();
    }

    private double calculateScore(Map<String, Object> drop) {
        int canClearScore = (int) drop.get("canClear");
        int heightScore = (int) drop.get("height");
        int gapsScore = (int) drop.get("gaps");
        int bumpinessScore = (int) drop.get("bumpiness");
        int floorTouchingBlocksScore = (int) drop.get("floorTouchingBlocks");
        int wallTouchingBlocksScore = (int) drop.get("wallTouchingBlocks");
        int edgesTouchingBlocksScore = (int) drop.get("edgesTouchingBlocks");

        // System.out.println("Can clear score: " + canClearScore);
        // System.out.println("Height score: " + heightScore);
        // System.out.println("Gaps score: " + gapsScore);

        // You can adjust the weights for each criterion based on importance
        // TODO: work on weights and criteria
        double totalScore = 2.7 * canClearScore + -3.71 * heightScore + -4.79 * gapsScore
                + 4.8 * edgesTouchingBlocksScore + 3.22 * wallTouchingBlocksScore + 3.68 * floorTouchingBlocksScore + -2.0 * bumpinessScore;
        // -3.4181268101392694f*canClearScore + 7.899265427351652f*heightScore + -2.5 *
        // bumpinessScore;
        // 4.500158825082766f*gapsScore;

        return totalScore;
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        Tetris tetris = bot.tetris; // Access the Tetris instance
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }

    // Helper Functions

    public void setTetris(Tetris tetris) {
        this.tetris = tetris;
    }
}