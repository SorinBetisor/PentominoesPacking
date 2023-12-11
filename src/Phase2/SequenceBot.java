package Phase2;

/**
 * The SequenceBot class represents a bot that plays the Tetris game using a predefined sequence of moves.
 * It interacts with the Tetris class to access the game field and current piece information.
 */
public class SequenceBot {
    public int[][] workingField;
    public Tetris tetris;

    /**
     * Constructs a new SequenceBot object.
     * Initializes the Tetris game and retrieves the working field.
     */
    public SequenceBot() {
        tetris = new Tetris();
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    /**
     * Runs the bot to play the Tetris game using a sequence of predefined pieces.
     * 
     * @param field         the current state of the game field
     * @param currentPiece  the current piece to be placed on the field
     * @param fWidth        the width of the game field
     * @param fHeight       the height of the game field
     */
    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
        Tetris.sequence = true;
        tetris.botPlaying = true;
        while (!tetris.gameOver) {
            try {
                Thread.sleep(tetris.getUpdatedPieceVelocity() * 2);
                placeOptimalPiece();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Places the optimal piece on the Tetris board based on the current ID.
     * The method uses a switch statement to determine the appropriate actions for each ID. (HardCoded)
     */
    public void placeOptimalPiece()
    {
        switch (tetris.currentID) {
            case 9:
                tetris.rotateRight();
                tetris.rotateRight();
                tetris.dropPiece();
                break;
            case 2:
                tetris.rotateRight();
                tetris.moveRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 5:
                tetris.rotateLeft();
                tetris.moveRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 7:
                tetris.rotateRight();
                tetris.dropPiece();
                break;
            case 11:
                tetris.rotateLeft();
                tetris.dropPiece();
            case 6:
                tetris.rotateRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 10:
                tetris.moveRight();
                tetris.moveRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 3:
                tetris.dropPiece();
                break;
            case 1:
                tetris.rotateRight();
                tetris.moveRight();
                tetris.moveRight();
                tetris.moveRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 8:
                tetris.rotateLeft();
                tetris.moveRight();
                tetris.moveRight();
                tetris.dropPiece();
                break;
            case 0:
                tetris.dropPiece();
                break;
            case 4:
                tetris.dropPiece();
                break;
            default:
                break;
        }
    }

    /**
     * The main method of the SequenceBot class.
     * It initializes the Tetris game with sequence mode enabled,
     * creates an instance of the SequenceBot class, and runs the bot.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
                Tetris.sequence = true;

        SequenceBot bot = new SequenceBot();
        Tetris tetris = bot.tetris;
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }
}
