package Phase2;

import javax.imageio.ImageIO;
import javax.swing.*;

import Phase2.helperClasses.ImagePanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.font.TextLayout;

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
            scorePanel = new ImagePanel("/Phase2/misc/bottomblu.jpg");
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

    // Paints the game screen
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
                g2d.setColor(GetColorOfID(state[i][j]));
                g2d.fill(new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));
                g2d.setColor(Color.GRAY);
                g2d.draw(new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));
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

        for (int i = 0; i < tetris.currentPiece.length; i++) {
            for (int j = 0; j < tetris.currentPiece[0].length; j++) {
                if (tetris.currentPiece[i][j] != 0) {
                    int y = tetris.getLowestY();
                    // System.out.println(y);
                    // if (Tetris.field[i + Tetris.currentY][j + y] != -1) {
                    if(tetris.field[i + tetris.currentX][j + (15-y)] != -1){
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3.0f));}
                    else{
                    // } else {
                        Color pieceColor = new Color(GetColorOfID(tetris.currentID).getRed(),
                        GetColorOfID(tetris.currentID).getGreen(),
                        GetColorOfID(tetris.currentID).getBlue(),
                        200);
                    g2d.setColor(pieceColor.darker());
                    g2d.setStroke(new BasicStroke(3.0f));
                    // }
                    
                }
                g2d.draw(new Rectangle2D.Double((i + leftFillerWidth + tetris.currentX) * size+1,
                            (j+(15-y)) * size, size, size));}
            }
        }
    }

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
            return new Color(0, 0, 0);
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

    // Sets the state of the game board
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

    // Shows game over message
    public void showGameOver() {
        JOptionPane.showMessageDialog(null,
                "Game Over! Your score is: " + tetris.score + "\nPress R to restart the game.");
    }

    // Key listener methods
    @Override
    public void keyTyped(KeyEvent e) {
    }

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
