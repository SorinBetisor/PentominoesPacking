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
    private int piecesElapsed;

    public double[] weights;

    public Bot(double[] weights) {
        tetris = new Tetris();
        this.weights = weights;
        piecesElapsed = 0;
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        tetris.botPlaying = true;
        while (!tetris.gameOver) {
            getAllPossibleDrops(tetris.getWorkingField(), currentPiece);
            try {
                Thread.sleep(tetris.getUpdatedPieceVelocity() * 2);
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
                int[][] simulatedBoard = Matrix.rotateMatrix(tetris.getSimulatedDropField(tetris.field));
                drops.add(new HashMap<String, Object>() {
                    {
                        put("rotation", currentRotation);
                        put("xpos", currentXPos);
                        // put("simulatedBoard", simulatedBoard);
                        put("canClear", Criteria.calculateClearRows(simulatedBoard));
                        put("height", Criteria.calculateHeight(simulatedBoard));
                        put("gaps", Criteria.calculateGaps(simulatedBoard));
                        put("bumpiness", Criteria.calculateBumpiness());
                        put("blocksAboveGaps", Criteria.calculateBlocksAboveGaps(simulatedBoard));
                        put("floorTouchingBlocks", Criteria.calculateFloorTouchingBlocks(simulatedBoard));
                        put("wallTouchingBlocks", Criteria.calculateWallTouchingBlocks(simulatedBoard));
                        put("edgesTouchingBlocks", Criteria.calculateEdgesTouchingBlocks(simulatedBoard));
                        put("rowTransitions", Criteria.calculateRowTransitions(simulatedBoard));
                        put("columnTransitions", Criteria.calculateColumnTransitions(simulatedBoard));
                    }
                });
                tetris.moveRight();
            }
            tetris.moveLeftToTheBorder();
            tetris.rotateRight();
            // System.out.print("\033[H\033[2J");
            // System.out.println(drops);
        }
        int bestMoveIndex = calculateBestMove(drops);
        tetris.rotateLeft();
        tetris.rotateLeft();
        tetris.rotateLeft();

        if (bestMoveIndex != -1) {
            // if(piecesElapsed == 0 && tetris.currentID == 4)
            // {
            //     tetris.rotateRight();
            //     tetris.rotateRight();
            // }
            performBestDrop();
        }
        piecesElapsed++;
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
        // System.out.println("Best move index: " + bestMoveIndex);
        // System.out.println("Best move score: " + maxScore);
        // System.out.println("Current best rotation: " + currentBestRotation);
        // System.out.println("Current best x position: " + currentBestXPos);
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
        // Object blocksAboveGapsObj = drop.get("blocksAboveGaps");
        // int blocksAboveGapsScore = (blocksAboveGapsObj != null) ? (int) blocksAboveGapsObj : 0;
        // System.out.println(blocksAboveGapsScore);
        // System.out.println("Can clear score: " + canClearScore);
        // System.out.println("Height score: " + heightScore);
        // System.out.println("Gaps score: " + gapsScore);

        double totalScore = weights[0] * heightScore + weights[1] * canClearScore + weights[2] * gapsScore + weights[3] * bumpinessScore
                + weights[4] * floorTouchingBlocksScore + weights[5] * wallTouchingBlocksScore + weights[6] * edgesTouchingBlocksScore;
        return totalScore;
    }

    public static void main(String[] args) {
        
        Bot bot = new Bot(new double[] { -3.71, 2.7, -4.79, -2.9, 4.98, 3.22, 4.8});
        //double totalScore = 2.7 * canClearScore + -3.71 * heightScore + -4.79 * gapsScore
        // + 4.8 * edgesTouchingBlocksScore + 3.22 * wallTouchingBlocksScore + 4.98 * floorTouchingBlocksScore + -2.9 * bumpinessScore;
        //Bot Weights: -4.600673152844697 3.310091433303091 -0.12420655245284395 0.0028305865270259467 2.594705313488695 3.8407725676586573 1.6460572978501622 4.850516530488642
        //Bot 5 Weights: -2.5597737207890123 2.4475682375585786 3.738909159509415 -2.920070396573741 1.3458430406816955 1.3983726844940332 4.651933572309237 4.243388641986979
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