package Phase2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Phase2.helperClasses.Criteria;
import Phase2.helperClasses.Matrix;

public class Bot {
    public int[][] workingField;
    public Tetris tetris;
    private int fitness;

    private int currentBestRotation;
    private int currentBestXPos;

    public double[] weights;

    public Bot(double[] weights) {
        tetris = new Tetris();
        this.weights = weights;
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        tetris.botPlaying = true;
        while (!tetris.gameOver) {
            getAllPossibleDrops(tetris.getWorkingField(), currentPiece);
            try {
                Thread.sleep((int)(tetris.getUpdatedPieceVelocity() * 2.5));
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
        int maxRotations = 3;
        if (tetris.currentID == 0) {
            maxRotations = 0;
        } else if (tetris.currentID == 1) {
            maxRotations = 1;
        }
        for (int rotation = 0; rotation <= 3; rotation++) { // rotations.size()
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
        int bestMoveIndex = calculateBestMove(drops);
        if (bestMoveIndex != -1)
            performBestDrop();

    }

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
        System.out.println("Best Move: " + drops.get(bestMoveIndex));
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