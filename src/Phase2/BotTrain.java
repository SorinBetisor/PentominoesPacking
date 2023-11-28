package Phase2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BotTrain {
    private static final int NUM_BOTS = 100;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_BOTS);
        List<Bot> bots = new ArrayList<>();

        for (int i = 0; i < NUM_BOTS; i++) {
            double[] weights = generateRandomArray();
            Bot bot = new Bot(weights);
            bots.add(bot);

            Runnable botTask = () -> runBot(bot, "Bot " + (bots.indexOf(bot) + 1));
            executorService.submit(botTask);
        }

        executorService.shutdown();
        System.exit(0);
    }

    public static void runBot(Bot bot, String botName) {
        Tetris tetris = bot.tetris;
        
        // Shuffle Tetris pieces before running the bot
        // System.out.println(shufflePieces(tetris.PIECES));
        // tetris.PIECES = (shufflePieces(tetris.PIECES));
        // System.out.println(tetris.PIECES);
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
        // System.out.println(tetris.PIECES);
        if (tetris.checkGameOver()) {
            if(tetris.score >= 10){
                System.out.println(botName + " Score: " + tetris.score);
                System.out.println(botName + " Weights: " + arrayToString(bot.weights));
                System.out.println("Sequence: " + Arrays.toString(tetris.PIECES));
            }
        }
    }

    // ... (rest of your code)

    public static String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for (double value : array) {
            sb.append(value).append(" ");
        }
        return sb.toString();
    }

    public static double[] generateRandomArray() {
        int arraySize = 7;
        double[] randomArray = new double[arraySize];
        Random random = new Random();
    
        for (int i = 0; i < arraySize; i++) {
            // Generate a random double value between -2.0 (inclusive) and 3.0 (exclusive)
            randomArray[i] = (random.nextDouble() * 5.0) - 2.0;
        }
    
        return randomArray;
    }
    
    
    
    
    
    
}
