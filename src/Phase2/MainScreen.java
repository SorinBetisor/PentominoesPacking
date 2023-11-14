package Phase2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class MainScreen extends JPanel implements KeyListener {
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
    private ImageIcon icon;

    public MainScreen(int x, int y, int _size, int[][] upcomingMatrix) {
        size = _size;
        this.x = x;
        this.y = y;
        this.upcomingMatrix = upcomingMatrix;

        try {
            leftFillerImage = ImageIO.read(getClass().getResource("/Phase2/misc/leftfiller.jpg"));
            rightFillerImage = ImageIO.read(getClass().getResource("/Phase2/misc/rightfiller.jpg"));
            icon = new ImageIcon(getClass().getResource("/Phase2/misc/icon.png")); // Use ImageIcon
        } catch (IOException e) {
            System.out.println("Error reading filler image");
            e.printStackTrace();
        }

        int panelWidth = (x + leftFillerWidth + rightFillerWidth) * size;
        setPreferredSize(new Dimension(panelWidth, (y + 1) * size));

        // Set the layout manager for MainScreen to BorderLayout
        setLayout(new BorderLayout());


        //TODO: work on bottom label
        // Create a panel to hold the score label and set its background to yellow
        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.MAGENTA.darker().darker().darker());
        // Create the Score label and add it to the scorePanel
        scoreLabel = new JLabel("Score: " + Tetris.score + "     Speed:" + (8 - (Tetris.pieceVelocity / 100)));
        scoreLabel.setHorizontalAlignment(JLabel.LEFT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(Color.MAGENTA.darker().darker().darker());
        scorePanel.add(scoreLabel);
        // Add the scorePanel to the MainScreen panel at the bottom
        add(scorePanel, BorderLayout.SOUTH);


        window = new JFrame("Pentomino Tetris Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(this);
        window.pack();
        window.setVisible(true);
        window.addKeyListener(this);
        window.setIconImage(icon.getImage());

        state = new int[x][y];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = -1;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(getVisibleRect());

        g2d.drawImage(leftFillerImage, 0, 0, leftFillerWidth * size, y * size, null);
        g2d.drawImage(rightFillerImage, (x + leftFillerWidth) * size, 0, rightFillerWidth * size, y * size, null);

        g2d.setColor(Color.GRAY);

        for (int i = leftFillerWidth; i <= (x + leftFillerWidth); i++) {
            g2d.drawLine(i * size, 0, i * size, y * size);
        }

        for (int i = 0; i <= y; i++) {
            g2d.drawLine(leftFillerWidth * size, i * size, (x + leftFillerWidth) * size, i * size);
        }

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                g2d.setColor(GetColorOfID(state[i][j]));
                g2d.fill(new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));
                g2d.setColor(Color.GRAY);
                g2d.draw(new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));
            }
        }

        int xOffset = (x + leftFillerWidth) * size;

        for (int i = 0; i < upcomingMatrix.length; i++) {
            for (int j = 0; j < upcomingMatrix[0].length; j++) {
                g2d.setColor(GetColorOfID(upcomingMatrix[i][j]));
                g2d.fill(new Rectangle2D.Double((i + xOffset) * size + 1, j * size + 1, size - 1, size - 1));
                g2d.setColor(Color.GRAY);
                g2d.draw(new Rectangle2D.Double((i + xOffset) * size + 1, j * size + 1, size - 1, size - 1));
            }
        }

        Font font = new Font("Dialog", Font.BOLD, 18);
        String highScoreString = "High Score: " + Tetris.highScore;

        // Create a gradient for the text color
        GradientPaint textGradient = new GradientPaint(
                0, 0, Color.ORANGE.brighter(),
                font.getSize() * highScoreString.length(), 0, Color.MAGENTA);

        TextLayout textLayout = new TextLayout(highScoreString, font, g2d.getFontRenderContext());

        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1.0f)); // Adjust the width of the stroke as needed

        // Draw the outline
        g2d.setColor(Color.BLACK);
        g2d.draw(textLayout.getOutline(AffineTransform.getTranslateInstance(10, 40)));

        // Reset the stroke
        g2d.setStroke(oldStroke);

        g2d.setPaint(textGradient);
        g2d.setFont(font);

        // Draw the filled text with gradient
        g2d.drawString(highScoreString, 10, 40);

        String scoreString = "Score: " + Tetris.score;
        ;

        // Create a gradient for the text color
        GradientPaint scoreTextGradient = new GradientPaint(
                0, 0, Color.GREEN.brighter(),
                font.getSize() * scoreString.length(), 0, Color.BLUE);

        TextLayout scoreTextLayout = new TextLayout(scoreString, font, g2d.getFontRenderContext());

        // Draw the outline for the score label
        g2d.setColor(Color.BLACK);
        g2d.draw(scoreTextLayout.getOutline(AffineTransform.getTranslateInstance(10, 65)));
        g2d.setStroke(oldStroke);
        g2d.setPaint(scoreTextGradient);
        g2d.setFont(font);
        g2d.drawString(scoreString, 10, 65);
        String speedString = "Speed: " + (10 - (Tetris.pieceVelocity / 100));

        // Create a gradient for the text color
        GradientPaint speedTextGradient = new GradientPaint(
                0, 0, Color.CYAN.brighter(),
                font.getSize() * speedString.length(), 0, Color.BLUE);

        TextLayout speedTextLayout = new TextLayout(speedString, font, g2d.getFontRenderContext());

        // Draw the outline for the speed label on the right side
        g2d.setColor(Color.BLACK);
        g2d.draw(
                speedTextLayout.getOutline(AffineTransform.getTranslateInstance(10, 87)));

        g2d.setStroke(oldStroke);

        g2d.setPaint(speedTextGradient);
        g2d.setFont(font);

        // Draw the filled text for the speed label on the right side with gradient
        g2d.drawString(speedString, 10, 87);
    }

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

    public static void updateScore() {
        scoreLabel.setText("Score: " + Tetris.score + "     Speed:" + (10 - (Tetris.pieceVelocity / 100)));
    }

    public static void updateSpeed() {
        scoreLabel.setText("Score: " + Tetris.score + "     Speed:" + (10 - (Tetris.pieceVelocity / 100)));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (!Tetris.gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Tetris.moveLeft();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Tetris.moveRight();
            } else if (keyCode == KeyEvent.VK_D) {
                Tetris.rotateRight();
            } else if (keyCode == KeyEvent.VK_A) {
                Tetris.rotateLeft();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Tetris.accelerateMovingDown();
            } else if (keyCode == KeyEvent.VK_UP) {
                Tetris.decelerateMovingDown();
            } else if (keyCode == KeyEvent.VK_SPACE) {
                Tetris.dropPiece();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
