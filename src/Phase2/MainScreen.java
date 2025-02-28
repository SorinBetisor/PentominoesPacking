package Phase2;

import javax.imageio.ImageIO;
import javax.swing.*;

import Phase2.helperClasses.ImagePanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainScreen class represents the main game screen of the Pentomino Tetris
 * game.
 * It extends JPanel and implements KeyListener to handle user input.
 * The class is responsible for painting the game screen, updating the score and
 * speed labels,
 * and showing the game over message.
 */
public class MainScreen extends JPanel implements KeyListener {
    // Instance variables
    private JFrame window;
    private int[][] state;
    private int size;
    private int leftFillerWidth = 3;
    private int rightFillerWidth = 3;
    private int x;
    private int y;
    private int[][] upcomingMatrix;
    public static JLabel scoreLabel;

    private BufferedImage leftFillerImage;
    private BufferedImage rightFillerImage;
    private ImagePanel scorePanel;
    private ImageIcon icon;
    public Tetris tetris;
    public static String playerName;

    // Constructor
    public MainScreen(int x, int y, int _size, int[][] upcomingMatrix, Tetris tetris) {
        size = _size;
        this.x = x;
        this.y = y;
        this.upcomingMatrix = upcomingMatrix;
        this.tetris = tetris;

        // Load images
        try {
            leftFillerImage = ImageIO.read(getClass().getResource("/Phase2/misc/leftfiller.jpg"));
            rightFillerImage = ImageIO.read(getClass().getResource("/Phase2/misc/rightfiller.jpg"));
            icon = new ImageIcon(getClass().getResource("/Phase2/misc/icon.png"));
            scorePanel = new ImagePanel("/Phase2/misc/bottomblu.png");
        } catch (IOException e) {
            System.out.println("Error reading filler image");
            e.printStackTrace();
        }

        // Set panel dimensions and layout
        int panelWidth = (x + leftFillerWidth + rightFillerWidth) * size;
        setPreferredSize(new Dimension(panelWidth, (y + 1) * size));
        setLayout(new BorderLayout());

        // Create and configure JFrame
        window = new JFrame("Pentomino Tetris Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(this);
        window.pack();
        window.setVisible(true);
        window.addKeyListener(this);
        window.setIconImage(icon.getImage());
        window.setLocationRelativeTo(null);

        // Create and configure score label
        scoreLabel = new JLabel("\u200B PENTRIS", SwingConstants.CENTER);
        scoreLabel.setHorizontalAlignment(JLabel.LEFT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(new Color(0, 0, 0, 0));
        scorePanel.add(scoreLabel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.SOUTH);

        // Initialize state array
        state = new int[x][y];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = -1;
            }
        }
    }

    /**
     * Overrides the paintComponent method to customize the appearance of the component.
     * This method is responsible for painting the game board, grid lines, Tetris pieces, labels, and leaderboard.
     * 
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Fill background with light gray
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(getVisibleRect());

        // Draw left and right filler images
        g2d.drawImage(leftFillerImage, 0, 0, leftFillerWidth * size, y * size, null);
        g2d.drawImage(rightFillerImage, (x + leftFillerWidth) * size, 0, rightFillerWidth * size, y * size, null);

        // Draw grid lines
        g2d.setColor(Color.GRAY);
        for (int i = leftFillerWidth; i <= (x + leftFillerWidth); i++) {
            g2d.drawLine(i * size, 0, i * size, y * size);
        }
        for (int i = 0; i <= y; i++) {
            g2d.drawLine(leftFillerWidth * size, i * size, (x + leftFillerWidth) * size, i * size);
        }

        // Draw current state of the game board
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] != -1) { // Only draw for non-empty cells (Tetris pieces)
                    Color pieceColor = GetColorOfID(state[i][j]);
                    g2d.setColor(pieceColor);

                    // Fill the rectangle with a solid color
                    g2d.fill(
                            new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));

                    // Draw a thicker border of the Tetris piece's color
                    Stroke originalStroke = g2d.getStroke(); // Save the original stroke
                    g2d.setStroke(new BasicStroke(3.0f)); // Set the thickness of the border
                    g2d.setColor(pieceColor.darker().darker());
                    g2d.draw(new Rectangle2D.Double((i + leftFillerWidth) * size, j * size, size, size));

                    // Restore the original stroke
                    g2d.setStroke(originalStroke);
                }
            }
        }

        // Draw upcoming piece matrix
        int xOffset = (x + leftFillerWidth) * size;
        for (int i = 0; i < upcomingMatrix.length; i++) {
            for (int j = 0; j < upcomingMatrix[0].length; j++) {
                g2d.setColor(GetColorOfID(upcomingMatrix[i][j]));
                g2d.fill(new Rectangle2D.Double((i + xOffset) * size + 1, j * size + 1, size - 1, size - 1));
                g2d.setColor(Color.GRAY);
                g2d.draw(new Rectangle2D.Double((i + xOffset) * size + 1, j * size + 1, size - 1, size - 1));
            }
        }

        // Draw high score label
        Font font = new Font("Dialog", Font.BOLD, 18);
        String highScoreString = "High Score: " + tetris.highScore;
        GradientPaint textGradient = new GradientPaint(
                0, 0, Color.ORANGE.brighter(),
                font.getSize() * highScoreString.length(), 0, Color.MAGENTA);
        TextLayout textLayout = new TextLayout(highScoreString, font, g2d.getFontRenderContext());
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(Color.BLACK);
        g2d.draw(textLayout.getOutline(AffineTransform.getTranslateInstance(10, 40)));
        g2d.setStroke(oldStroke);
        g2d.setPaint(textGradient);
        g2d.setFont(font);
        g2d.drawString(highScoreString, 10, 40);

        // Draw score label
        String scoreString = "Score: " + tetris.score;
        GradientPaint scoreTextGradient = new GradientPaint(
                0, 0, Color.GREEN.brighter(),
                font.getSize() * scoreString.length(), 0, Color.BLUE);
        TextLayout scoreTextLayout = new TextLayout(scoreString, font, g2d.getFontRenderContext());
        g2d.setColor(Color.BLACK);
        g2d.draw(scoreTextLayout.getOutline(AffineTransform.getTranslateInstance(10, 65)));
        g2d.setStroke(oldStroke);
        g2d.setPaint(scoreTextGradient);
        g2d.setFont(font);
        g2d.drawString(scoreString, 10, 65);

        // Draw speed label
        String speedString = "Speed: " + (10 - (tetris.pieceVelocity / 100));
        GradientPaint speedTextGradient = new GradientPaint(
                0, 0, Color.CYAN.brighter(),
                font.getSize() * speedString.length(), 0, Color.BLUE);
        TextLayout speedTextLayout = new TextLayout(speedString, font, g2d.getFontRenderContext());
        g2d.setColor(Color.BLACK);
        g2d.draw(speedTextLayout.getOutline(AffineTransform.getTranslateInstance(10, 87)));
        g2d.setStroke(oldStroke);
        g2d.setPaint(speedTextGradient);
        g2d.setFont(font);
        g2d.drawString(speedString, 10, 87);

        int leaderboardWidth = rightFillerWidth * (int) (0.85 * size);
        int leaderboardHeight = 200; // Adjust the height as needed
        int leaderboardX = (x + leftFillerWidth) * size + (int) (0.2 * size);
        ;
        int leaderboardY = 10;

        g2d.setColor(Color.WHITE);
        g2d.drawRect(leaderboardX - 1, leaderboardY - 1, leaderboardWidth + 1, leaderboardHeight + 2);
        g2d.setColor(Color.MAGENTA.darker().darker());
        g2d.fillRect(leaderboardX, leaderboardY, leaderboardWidth, leaderboardHeight);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString("Leaderboard", leaderboardX + 10, leaderboardY + 20);

        // Read existing high scores from the file
        String path1 = "bcs_group_33_project_2023\\src\\Phase2\\highscores.txt";
        String path2 = "src/Phase2/highscores.txt";

        List<Player> players = null;

        if (fileExists(path1)) {
            players = Player.readHighScores(path1);
        } else if (fileExists(path2)) {
            players = Player.readHighScores(path2);
        } else {
            System.out.println("Both paths do not exist.");
            players = new ArrayList<>();
            players.add(new Player("Grish", 34));
            players.add(new Player("Alex", 20));
            players.add(new Player("Jana", 13));
            players.add(new Player("Sorin", 12));
            players.add(new Player("dan", 9));
        }

        // Display the first 5 players in the leaderboard
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 16));
        int yOffset = 40; // Adjust the starting y-coordinate for player entries

        for (int i = 0; i < Math.min(8, players.size()); i++) {
            Player player = players.get(i);
            String leaderboardEntry = player.getName() + ": " + player.getHighScore();
            g2d.drawString(leaderboardEntry, leaderboardX + 10, leaderboardY + yOffset);
            yOffset += 20; // Adjust the vertical spacing between entries
        }

        if(!tetris.botPlaying || !Tetris.sequence){
        int nextPieceXOffset = leaderboardX + 10;
        int nextPieceYOffset = leaderboardY + leaderboardHeight + 20;
        int nextPieceSize = rightFillerWidth * (int) (0.65 * size);

        int borderThickness2 = 2;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(nextPieceXOffset - borderThickness2, nextPieceYOffset - borderThickness2,
                nextPieceSize + 2 * borderThickness2, nextPieceSize + 2 * borderThickness2);

        // Draw the white rectangle background
        g2d.setColor(Color.MAGENTA.darker().darker());
        g2d.fillRect(nextPieceXOffset, nextPieceYOffset, nextPieceSize, nextPieceSize);

        String labelText = "Next Piece";
        Font labelFont = new Font("Monospaced", Font.BOLD, 14);

        // Define the gradient for the text
        GradientPaint labelGradient = new GradientPaint(
                nextPieceXOffset, nextPieceYOffset + nextPieceSize + 20, Color.WHITE.brighter(),
                nextPieceXOffset + labelFont.getSize() * labelText.length(), nextPieceYOffset + nextPieceSize + 20,
                Color.WHITE);

        // Draw the border for the text
        g2d.setColor(Color.BLACK);
        g2d.setFont(labelFont);

        int borderThickness = 1;
        for (int i = -borderThickness; i <= borderThickness; i++) {
            for (int j = -borderThickness; j <= borderThickness; j++) {
                if (i != 0 || j != 0) {
                    g2d.drawString(labelText, nextPieceXOffset + i, nextPieceYOffset + nextPieceSize + 20 + j);
                }
            }
        }

        // Draw the text with the gradient
        g2d.setPaint(labelGradient);
        g2d.drawString(labelText, nextPieceXOffset, nextPieceYOffset + nextPieceSize + 20);

        // Draw the lookAheadPiece inside the white square
        int pieceSize = (int) (size * 0.4); // Adjust the piece size as needed
        int padding = 2; // Padding between pieces in the lookahead area
        int pieceID = tetris.nextPieceID;

        if (pieceID == 1) {
            pieceSize = (int) (size * 0.35);
            nextPieceYOffset += 30;
        } else {
            pieceSize = (int) (size * 0.4);
            nextPieceYOffset = leaderboardY + leaderboardHeight + 20;
        }

        for (int i = 0; i < tetris.lookAheadPiece.length; i++) {
            for (int j = 0; j < tetris.lookAheadPiece[0].length; j++) {

                int xCoordinate;
                int yCoordinate;
                if (pieceID == 1) {
                    xCoordinate = nextPieceXOffset + i * pieceSize + padding * i;
                    yCoordinate = 8 + nextPieceYOffset + j * pieceSize + padding * j;
                }
                else
                {
                    xCoordinate = 8+nextPieceXOffset + i * pieceSize + padding * i;
                    yCoordinate = 8 + nextPieceYOffset + j * pieceSize + padding * j;
                }

                if (tetris.lookAheadPiece[i][j] == 0) {
                    // Draw a white rectangle
                    g2d.setColor(Color.MAGENTA.darker().darker());
                    g2d.fill(new Rectangle2D.Double(xCoordinate, yCoordinate, pieceSize, pieceSize));

                    // Draw the border if needed
                    // g2d.setColor(Color.GRAY);
                    // g2d.draw(new Rectangle2D.Double(xCoordinate, yCoordinate, pieceSize,
                    // pieceSize));
                } else {
                    // Draw a colored rectangle based on pieceID
                    g2d.setColor(GetColorOfID(pieceID).brighter());
                    g2d.fill(new Rectangle2D.Double(xCoordinate, yCoordinate, pieceSize, pieceSize));

                    // Draw the border if needed
                    // g2d.setColor(Color.GRAY);
                    // g2d.draw(new Rectangle2D.Double(xCoordinate, yCoordinate, pieceSize,
                    // pieceSize));
                }
            }
        }
    }
        if (!tetris.botPlaying) {
            for (int i = 0; i < tetris.currentPiece.length; i++) {
                for (int j = 0; j < tetris.currentPiece[0].length; j++) {
                    if (tetris.currentPiece[i][j] != 0) {
                        int y = tetris.getLowestY();
                        // System.out.println(y);
                        // if (Tetris.field[i + Tetris.currentY][j + y] != -1) {
                        if (tetris.field[i + tetris.currentX][j + (Tetris.VERTICAL_GRID_SIZE - y)] != -1) {
                            g2d.setColor(Color.WHITE);
                            g2d.setStroke(new BasicStroke(3.0f));
                        } else {
                            // } else {
                            Color pieceColor = new Color(GetColorOfID(tetris.currentID).getRed(),
                                    GetColorOfID(tetris.currentID).getGreen(),
                                    GetColorOfID(tetris.currentID).getBlue(),
                                    200);
                            g2d.setColor(pieceColor.darker());
                            g2d.setStroke(new BasicStroke(3.0f));
                            // }

                        }
                        g2d.draw(new Rectangle2D.Double((i + leftFillerWidth + tetris.currentX) * size + 1,
                                (j + (Tetris.VERTICAL_GRID_SIZE - y)) * size, size, size));
                    }
                }
            }
        }
    }

    /**
     * Checks if a file exists at the specified path.
     * 
     * @param path the path of the file to check
     * @return true if the file exists and is a regular file, false otherwise
     */
    public static boolean fileExists(String path) {
        Path filePath = Paths.get(path);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    
    /** 
     * @param i
     * @return Color
     */
    // Returns the color associated with a given ID
    private Color GetColorOfID(int i) {
        if (i == 0) {
            return Color.BLUE;
        } else if (i == 1) {
            return Color.ORANGE;
        } else if (i == 2) {
            return Color.CYAN;
        } else if (i == 3) {
            return Color.GREEN;
        } else if (i == 4) {
            return Color.MAGENTA;
        } else if (i == 5) {
            return Color.PINK;
        } else if (i == 6) {
            return Color.RED;
        } else if (i == 7) {
            return Color.YELLOW;
        } else if (i == 8) {
            return Color.LIGHT_GRAY.darker().darker();
        } else if (i == 9) {
            return new Color(0, 0, 100);
        } else if (i == 10) {
            return new Color(100, 0, 0);
        } else if (i == 11) {
            return new Color(0, 100, 0);
        } else {
            return Color.LIGHT_GRAY;
        }
    }

    /**
     * Sets the state of the MainScreen with the provided 2D integer array.
     * The provided array should have the same dimensions as the current state
     * array.
     * The elements of the provided array will be copied to the corresponding
     * positions in the state array.
     * After setting the state, the MainScreen will be repainted.
     *
     * @param _state the 2D integer array representing the new state of the
     *               MainScreen
     */
    public void setState(int[][] _state) {
        int rows = Math.min(state.length, _state.length);
        int cols = Math.min(state[0].length, _state[0].length);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                state[i][j] = _state[i][j];
            }
        }

        repaint();
    }

    // Updates the score label
    public static void updateScore() {
        scoreLabel.setText("SCORE ++");
    }

    // Updates the speed label
    public void updateSpeed() {
        scoreLabel.setText("Score: " + tetris.score + "     Speed:" + (10 - (tetris.pieceVelocity / 100)));
    }

    /**
     * Displays a game over message with the player's score and a prompt to restart
     * the game.
     */
    public void showGameOver() {
        if (!tetris.botPlaying) {
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = JOptionPane.showInputDialog(null, "Enter your name:");
            }

            JOptionPane.showMessageDialog(null,
                    "Game Over, " + playerName + "! Your score is: " + tetris.score + "\nPress R to restart the game.");

            Player player = new Player(playerName, tetris.highScore);
            tetris.player = player;

            player.updateHighScores(playerName, tetris.highScore);
        } else {
            JOptionPane.showMessageDialog(null, "Game Over! The bot got the score:  " + tetris.score);
            // window.dispose();
        }
    }

    // Key listener methods
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key is pressed.
     * 
     * @param e the KeyEvent object representing the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (!tetris.gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                tetris.moveLeft();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                tetris.moveRight();
            } else if (keyCode == KeyEvent.VK_D) {
                tetris.rotateRight();
            } else if (keyCode == KeyEvent.VK_A) {
                tetris.rotateLeft();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                tetris.accelerateMovingDown();
            } else if (keyCode == KeyEvent.VK_UP) {
                tetris.decelerateMovingDown();
            } else if (keyCode == KeyEvent.VK_SPACE) {
                tetris.dropPiece();
            }
        } else {
            if (keyCode == KeyEvent.VK_R) {
                window.dispose();
                tetris.gameOver = false;
                tetris.score = 0;
                tetris.pieceVelocity = 1000;
                Tetris tetris = new Tetris();
                tetris.restartTetris();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
