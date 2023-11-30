package Phase2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player {
    private String name;
    private int currentScore = 0;
    private int highScore;

    public Player(String name, int highScore) {
        this.name = name;
        this.highScore = highScore;
    }

    public void updateHighScores(String playerName, int score) {
        // File path to store high scores
        String filePath = "bcs_group_33_project_2023\\src\\Phase2\\highscores.txt";

        // Read existing high scores from the file
        List<Player> players = readHighScores(filePath);

        // Check if the player already exists
        boolean playerExists = false;
        for (Player player : players) {
            if (player.name.equals(playerName)) {
                playerExists = true;
                // Update the high score only if the new score is higher
                if (score > player.highScore) {
                    player.highScore = score;
                }
                break;
            }
        }

        // If the player doesn't exist, add a new entry
        if (!playerExists) {
            players.add(new Player(playerName, score));
        }

        // Sort the players based on high scores in descending order
        players.sort(Comparator.comparingInt(player -> -player.highScore));

        // Write the updated high scores back to the file
        writeHighScores(filePath, players);
    }

    // Function to read high scores from a file
    public static List<Player> readHighScores(String filePath) {
        List<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int highScore = Integer.parseInt(parts[1].trim());
                    players.add(new Player(name, highScore));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return players;
    }

    // Function to write high scores to a file
    private void writeHighScores(String filePath, List<Player> players) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Player player : players) {
                writer.write(player.name + "," + player.highScore);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public Player(String name) {
        this.name = name;
    }
}
