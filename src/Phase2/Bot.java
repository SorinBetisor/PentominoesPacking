package Phase2;

import java.util.Arrays;

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
        workingField = null;
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        Tetris.botPlaying = true;
        while (!Tetris.gameOver) {
            // System.out.println(Criteria.calculateHeight(botGame.getWorkingField()));
            // System.out.println(Arrays.deepToString(botGame.getWorkingField()));
            // System.out.println(Criteria.calculateGaps(botGame.getWorkingField()));
            try {
            Thread.sleep(1000);
            } catch (InterruptedException e) {
            e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        
        Bot bot = new Bot();

        bot.runBot(Tetris.field, Tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }

    // Helper Functions
}
