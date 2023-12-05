package Phase2;

public class SequenceBot {
    public int[][] workingField;
    public Tetris tetris;

    public SequenceBot() {
        tetris = new Tetris();
        tetris.runTetris();
        workingField = tetris.getWorkingField();
    }

    public void runBot(int[][] field, int[][] currentPiece, int fWidth, int fHeight) {
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

    public static void main(String[] args) {
        
        SequenceBot bot = new SequenceBot();
        Tetris tetris = bot.tetris;
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
    }
}
