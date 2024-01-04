package Phase2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Phase2.helperClasses.Criteria;
import Phase2.helperClasses.Matrix;

/**
 * The Bot class represents a bot player in the Tetris game. It uses a set of weights to make decisions on where to place the Tetris pieces.
 */
public class Bot {
    public int[][] workingField;
    public Tetris tetris;
    private int fitness;

    private int currentBestRotation;
    private int currentBestXPos;
    private static boolean firstPiece = true;

    public double[] weights;

    /**
     * Creates a new instance of the Bot class with the specified weights.
     * 
     * @param weights the weights used by the bot for decision making
     */
    public Bot(double[] weights) {
        tetris = new Tetris();
        this.weights = weights;
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    
    /** 
     * @param field
     * @param currentPiece
     * @param fWidth
     * @param fHeight
     */
    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        tetris.botPlaying = true;
        while (!tetris.gameOver) {
            if (firstPiece) {
                baseCases();
            }
            if (!firstPiece)
                getAllPossibleDrops(tetris.getWorkingField(), currentPiece);

            if (firstPiece)
                firstPiece = false;
            try {
                Thread.sleep((int) (tetris.getUpdatedPieceVelocity() * 1.95));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculates all possible drops for the current piece on the game field.
     * Each drop is represented as a map containing various criteria values.
     * The drops are stored in a list and later used to determine the best move.
     *
     * @param afield The game field represented as a 2D array.
     * @param currentPiece The current piece represented as a 2D array.
     */
    public void getAllPossibleDrops(int[][] afield, int[][] currentPiece) {

        List<Map<String, Object>> drops = new ArrayList<>();
        tetris.moveLeftToTheBorder();

        for (int rotation = 0; rotation <= 3; rotation++) {
            final int currentRotation = rotation;
            tetris.currentRotation = rotation;
            for (int xposition = 0; xposition <= (Tetris.HORIZONTAL_GRID_SIZE
                    - (currentPiece[0].length)); xposition++) {
                final int currentXPos = tetris.currentX;
                int[][] simulatedBoard = Matrix.rotateMatrix(tetris.getSimulatedDropField(tetris.field));
                drops.add(new HashMap<String, Object>() {
                    {
                        put("rotation", currentRotation);
                        put("xpos", currentXPos);
                        put("canClear", Criteria.calculateClearRows(simulatedBoard));
                        put("height", Criteria.calculateHeight(simulatedBoard));
                        put("gaps", Criteria.calculateGaps(simulatedBoard));
                        put("bumpiness", Criteria.calculateBumpiness(simulatedBoard));
                        put("blocksAboveGaps", Criteria.calculateBlocksAboveGaps(simulatedBoard));
                        put("floorTouchingBlocks", Criteria.calculateFloorTouchingBlocks(simulatedBoard));
                        put("wallTouchingBlocks", Criteria.calculateWallTouchingBlocks(simulatedBoard));
                        put("edgesTouchingBlocks", Criteria.calculateEdgesTouchingBlocks(simulatedBoard));
                    }
                });
                tetris.moveRight();
                // thread sleep
                // try {
                // Thread.sleep(30);
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // }
            }
            tetris.moveLeftToTheBorder(); // check this

            // thread sleep
            // try {
            // Thread.sleep(30);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            tetris.rotateRight();
            // thread sleep
            // try {
            // Thread.sleep(30);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // System.out.print("\033[H\033[2J");
            // System.out.println(drops);
        }
        tetris.moveLeftToTheBorder();
        calculateBestMove(drops);
        performBestDrop();
    }

    /**
     * Calculates the best move from a list of drops.
     * The best move is determined by the highest score calculated using the calculateScore method.
     * Updates the currentBestRotation and currentBestXPos variables with the rotation and xpos of the best move.
     *
     * @param drops the list of drops to evaluate
     * @return the index of the best move in the drops list
     */
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
        // System.out.println("Best Move: " + drops.get(bestMoveIndex));
        return bestMoveIndex;
    }

    /**
     * Performs the best drop by moving the current piece to the best position and rotation,
     * and then dropping it.
     */
    public void performBestDrop() {

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
        
        if (tetris.currentX != currentBestXPos && tetris.currentRotation != currentBestRotation) {
            tetris.currentX = currentBestXPos;
            tetris.currentRotation = currentBestRotation;
            tetris.addPiece(tetris.currentPiece, tetris.currentID, tetris.currentX, tetris.currentY);
        }

        tetris.dropPiece();
    }

    /**
     * Calculates the score based on the given drop parameters.
     *
     * @param drop a map containing the drop parameters including canClear, height, gaps, bumpiness,
     *             floorTouchingBlocks, wallTouchingBlocks, and edgesTouchingBlocks.
     * @return the calculated score.
     */
    private double calculateScore(Map<String, Object> drop) {
        int canClearScore = (int) drop.get("canClear");
        int heightScore = (int) drop.get("height");
        int gapsScore = (int) drop.get("gaps");
        int bumpinessScore = (int) drop.get("bumpiness");
        // int bumpinessScore = 0;
        int floorTouchingBlocksScore = (int) drop.get("floorTouchingBlocks");
        int wallTouchingBlocksScore = (int) drop.get("wallTouchingBlocks");
        int edgesTouchingBlocksScore = (int) drop.get("edgesTouchingBlocks");

        double totalScore = weights[0] * heightScore + weights[1] * canClearScore + weights[2] * gapsScore
                + weights[3] * bumpinessScore
                + weights[4] * floorTouchingBlocksScore + weights[5] * wallTouchingBlocksScore
                + weights[6] * edgesTouchingBlocksScore;
        return totalScore;
    }

    /**
     * Executes the base cases for the Tetris game.
     * The base cases include specific actions for different current piece IDs.
     * - If the current piece ID is 2, it rotates right and drops the piece.
     * - If the current piece ID is 10, it rotates right, moves right twice, and drops the piece.
     * - If the current piece ID is 8, it rotates right twice and drops the piece.
     * - If the current piece ID is 3, it rotates left three times, moves right twice, and drops the piece.
     * - If the current piece ID is 7, it rotates right twice, moves right, and drops the piece.
     * - If the current piece ID is 11, it rotates right and drops the piece.
     */
    public void baseCases() {
        if (tetris.currentID == 2) {
            tetris.rotateRight();
            tetris.dropPiece();
        } else if (tetris.currentID == 10) {
            tetris.rotateRight();
            tetris.moveRight();
            tetris.moveRight();
            tetris.dropPiece();
        } else if (tetris.currentID == 8) {
            tetris.rotateRight();
            tetris.rotateRight();
            tetris.dropPiece();
        } else if (tetris.currentID == 3) {
            tetris.rotateLeft();
            tetris.rotateLeft();
            tetris.rotateLeft();
            tetris.moveRight();
            tetris.moveRight();
            tetris.dropPiece();
        } else if (tetris.currentID == 7) {
            tetris.rotateRight();
            tetris.rotateRight();
            tetris.moveRight();
            tetris.dropPiece();
        } else if (tetris.currentID == 11) {
            tetris.rotateRight();
            tetris.dropPiece();
        }
    }

    public static void main(String[] args) {

        Bot bot = new Bot(new double[] { -3.71, 3.7, -4.79, -2.9, 4.98, 3.22, 4.8 });

        Tetris tetris = bot.tetris;
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }

    // Helper Functions

    public void setTetris(Tetris tetris) {
        this.tetris = tetris;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getFitness() {
        return fitness;
    }
}