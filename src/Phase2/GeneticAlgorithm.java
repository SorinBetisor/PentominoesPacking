package Phase2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int NUM_BOTS = 100;
    private static final int NUM_GENERATIONS = 100;
    private static final int TOURNAMENT_SIZE = 5;
    private static final double MUTATION_RATE = 0.1;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        // Initialization of population
        List<Bot> population = initializePopulation(NUM_BOTS);

        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            // Evaluate fitness for each bot in the population
            evaluateFitness(population);

            // Select parents for crossover
            List<Bot> parents = selectParents(population);

            // Create the next generation using crossover and mutation
            List<Bot> nextGeneration = createNextGeneration(parents);

            // Replace the old population with the new generation
            population = nextGeneration;
        }

        // Final evaluation of the last generation
        evaluateFitness(population);

        // Find and print the best bot
        Bot bestBot = population.stream().max(Comparator.comparingDouble(Bot::getFitness)).orElse(null);
        if (bestBot != null) {
            System.out.println("Best Bot - Score: " + bestBot.tetris.score);
            System.out.println("Weights: " + arrayToString(bestBot.weights));
        }
    }

    // Other methods remain the same...

    private static List<Bot> initializePopulation(int size) {
        List<Bot> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double[] weights = generateRandomArray();
            Bot bot = new Bot(weights);
            population.add(bot);
        }
        return population;
    }

    private static void evaluateFitness(List<Bot> population) {
        for (Bot bot : population) {
            runBot(bot, "Bot");
            // You may want to modify the fitness function based on your specific requirements
            bot.setFitness(bot.tetris.score);
        }
    }

    private static List<Bot> selectParents(List<Bot> population) {
        List<Bot> parents = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            // Tournament selection
            List<Bot> tournament = new ArrayList<>();
            for (int j = 0; j < TOURNAMENT_SIZE; j++) {
                tournament.add(population.get(new Random().nextInt(population.size())));
            }
            // Select the best bot from the tournament as a parent
            parents.add(tournament.stream().max(Comparator.comparingDouble(Bot::getFitness)).orElse(null));
        }
        return parents;
    }

    private static List<Bot> createNextGeneration(List<Bot> parents) {
        List<Bot> nextGeneration = new ArrayList<>();
        for (int i = 0; i < parents.size(); i += 2) {
            Bot parent1 = parents.get(i);
            Bot parent2 = parents.get(i + 1);
            double[] childWeights = crossover(parent1.weights, parent2.weights);
            mutate(childWeights);
            nextGeneration.add(new Bot(childWeights));
        }
        return nextGeneration;
    }

    private static double[] crossover(double[] parent1, double[] parent2) {
        // You may want to implement different crossover methods (e.g., one-point crossover)
        int crossoverPoint = new Random().nextInt(parent1.length);
        double[] child = Arrays.copyOf(parent1, parent1.length);
        System.arraycopy(parent2, 0, child, 0, crossoverPoint);
        return child;
    }

    private static void mutate(double[] weights) {
        for (int i = 0; i < weights.length; i++) {
            if (Math.random() < MUTATION_RATE) {
                // Introduce a small random change to the weight
                weights[i] += (Math.random() * 2.0 - 1.0);
            }
        }
    }

    public static void runBot(Bot bot, String botName) {
        Tetris tetris = bot.tetris;
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
        if (tetris.checkGameOver()) {
            if(tetris.score >= 8){
                System.out.println(botName + " Score: " + tetris.score);
                System.out.println(botName + " Weights: " + arrayToString(bot.weights));
                System.out.println("Sequence: " + Arrays.toString(tetris.PIECES));
            }
        }
    }
    
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
            // Generate a random double value between -5.0 (inclusive) and 5.0 (exclusive)
            randomArray[i] = (random.nextDouble() * 10.0) - 5.0;
        }
    
        return randomArray;
    }
}
