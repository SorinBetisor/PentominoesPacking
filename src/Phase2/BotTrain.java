package Phase2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BotTrain {
    private static final int NUM_BOTS = 60;
    private static final int NUM_GENERATIONS = 50;
    private static final int TOURNAMENT_SIZE = 20;
    private static final double MUTATION_RATE = 0.4;
    private static final Object fitnessLock = new Object();

    private static final int NUM_ELITE_BOTS = 70;
    private static List<Bot> eliteBots = new ArrayList<>();
    private static Bot bestBot;

    public static void main(String[] args) {

        List<Bot> population = initializePopulation(NUM_BOTS);

        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            ExecutorService executorService = Executors.newFixedThreadPool(NUM_BOTS);
            List<Bot> currentPopulation = new ArrayList<>(population);

            for (Bot bot : currentPopulation) {
                Runnable botTask = () -> runBot(bot, "Bot " + (currentPopulation.indexOf(bot) + 1));
                executorService.submit(botTask);
            }

            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            evaluateFitness(population);
            synchronized (fitnessLock) {
                eliteBots.addAll(population);
            }
            
            eliteBots.sort(Comparator.comparingDouble(Bot::getFitness).reversed());
            eliteBots = eliteBots.subList(0, Math.min(NUM_ELITE_BOTS, eliteBots.size()));

            updateBestBot(population);
            System.out.println("Top 10 Elite Bots:");
            for (int i = 0; i < Math.min(10, eliteBots.size()); i++) {
                Bot eliteBot = eliteBots.get(i);
                System.out.println("Bot " + (i + 1) + " - Score: " + eliteBot.tetris.score);
                System.out.println("Weights: " + arrayToString(eliteBot.weights));
            }
            List<Bot> parents = selectParents(population);

            List<Bot> nextGeneration = createNextGeneration(parents);

            population = nextGeneration;
        }

        evaluateFitness(population);

        if (bestBot != null) {
            System.out.println("Best Bot - Score: " + bestBot.tetris.score);
            System.out.println("Weights: " + arrayToString(bestBot.weights));
        }
    }

    private static void updateBestBot(List<Bot> population) {
        Bot currentBest = population.stream().max(Comparator.comparingDouble(Bot::getFitness)).orElse(null);
        if (bestBot == null || (currentBest != null && currentBest.getFitness() > bestBot.getFitness())) {
            bestBot = currentBest;
        }
    }

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
        List<Bot> currentPopulation = new ArrayList<>(population);
    
        synchronized (fitnessLock) {
            for (Bot bot : currentPopulation) {
                runBot(bot, "Bot");
                bot.setFitness(bot.tetris.score);
            }
        }
    }
    

    private static List<Bot> selectParents(List<Bot> population) {
        List<Bot> parents = new ArrayList<>();

        parents.addAll(eliteBots);
        for (int i = 0; i < NUM_BOTS - eliteBots.size(); i++) {
            List<Bot> tournament = new ArrayList<>();
            for (int j = 0; j < TOURNAMENT_SIZE; j++) {
                tournament.add(population.get(new Random().nextInt(population.size())));
            }
            parents.add(tournament.stream().max(Comparator.comparingDouble(Bot::getFitness)).orElse(null));
        }

        return parents;
    }

    private static List<Bot> createNextGeneration(List<Bot> parents) {
        List<Bot> nextGeneration = new ArrayList<>();
    
        // Select the top 70 bots from the elite bots
        nextGeneration.addAll(eliteBots.subList(0, Math.min(70, eliteBots.size())));
    
        // Add 30% new bots to the next generation
        int numRandomBots = (int) (0.3 * NUM_BOTS);
        for (int i = 0; i < numRandomBots; i++) {
            double[] newBotWeights = generateRandomArray();
            nextGeneration.add(new Bot(newBotWeights));
        }
    
        // Continue with crossover and mutation for the remaining parents
        for (int i = 0; i < parents.size() - 1; i += 2) {
            Bot parent1 = parents.get(i);
            Bot parent2 = parents.get(i + 1);
            double[] childWeights = crossover(parent1.weights, parent2.weights);
            mutate(childWeights);
            nextGeneration.add(new Bot(childWeights));
        }
    
        if (parents.size() % 2 != 0) {
            nextGeneration.add(parents.get(parents.size() - 1));
        }
    
        return nextGeneration;
    }
    

    private static double[] crossover(double[] parent1, double[] parent2) {
        int crossoverPoint = new Random().nextInt(parent1.length - 1) + 1;  // Ensure crossover point is not at the extremes
        double[] child = Arrays.copyOf(parent1, parent1.length);
        System.arraycopy(parent2, 0, child, 0, crossoverPoint);
        return child;
    }

    private static void mutate(double[] weights) {
        for (int i = 0; i < weights.length; i++) {
            if (Math.random() < MUTATION_RATE) {
                weights[i] += (Math.random() * 2.0 - 1.0);
            }
        }
    }

    public static void runBot(Bot bot, String botName) {
        Tetris tetris = bot.tetris;
        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
        if (tetris.checkGameOver()) {
            // if (tetris.score >= 8) {
            // System.out.println(botName + " Score: " + tetris.score);
            // System.out.println(botName + " Weights: " + arrayToString(bot.weights));
            // System.out.println("Sequence: " + Arrays.toString(tetris.PIECES));
            // }
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
        int arraySize = 8;
        double[] randomArray = new double[arraySize];
        Random random = new Random();

        for (int i = 0; i < arraySize; i++) {
            randomArray[i] = (random.nextDouble() * 10.0) - 5.0;
        }

        return randomArray;
    }

}
