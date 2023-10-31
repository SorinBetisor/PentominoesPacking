package Phase2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This class takes care of all the graphics to display a certain state.
 * Initially, you do not need to modify (or event understand) this class in
 * Phase 1. You will learn more about GUIs in Period 2, in the Introduction to
 * Computer Science 2 course.
 */
public class MainScreen extends JPanel {
    private JFrame window;
    private int[][] state;
    private int size;
    private int leftFillerWidth = 3;
    private int rightFillerWidth = 3;
    private int x;
    private int y;

    private BufferedImage leftFillerImage;
    private BufferedImage rightFillerImage;

    public MainScreen(int x, int y, int _size) {
        size = _size;
        this.x = x;
        this.y = y;

        try {
            leftFillerImage = ImageIO.read(getClass().getResource(
                    "/Phase2/misc/leftfiller.jpg"));
            rightFillerImage = ImageIO.read(getClass().getResource(
                    "/Phase2/misc/rightfiller.jpg"));
        } catch (IOException e) {
            System.out.println("Error reading filler image");
            e.printStackTrace();
        }

        // Add fillers to the panel's width
        int panelWidth = (x + leftFillerWidth + rightFillerWidth) * size;
        setPreferredSize(new Dimension(panelWidth, y * size));

        window = new JFrame("Pentomino Tetris Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(this);
        window.pack();
        window.setVisible(true);

        state = new int[x][y];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = -1;
            }
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D) g;

        localGraphics2D.setColor(Color.LIGHT_GRAY);
        localGraphics2D.fill(getVisibleRect());

        // Draw the filler image on the left side
        localGraphics2D.drawImage(leftFillerImage, 0, 0, leftFillerWidth*size, y * size, null);
        //draw image on the right side of the grid
        localGraphics2D.drawImage(rightFillerImage, size*8, 0, leftFillerWidth*size, y * size, null);

        // Draw lines to separate the cells
        localGraphics2D.setColor(Color.GRAY);

        for (int i = leftFillerWidth; i <= (x + leftFillerWidth); i++) {
            localGraphics2D.drawLine(i * size, 0, i * size, y * size);
        }

        for (int i = 0; i <= y; i++) {
            localGraphics2D.drawLine(leftFillerWidth * size, i * size, (x + leftFillerWidth) * size, i * size);
        }

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                localGraphics2D.setColor(GetColorOfID(state[i][j]));
                localGraphics2D.fill(
                        new Rectangle2D.Double((i + leftFillerWidth) * size + 1, j * size + 1, size - 1, size - 1));
            }
        }
    }

    /**
     * Decodes the ID of a pentomino into a color
     * 
     * @param i ID of the pentomino to be colored
     * @return the color to represent the pentomino. It uses the class Color (more
     *         in ICS2 course in Period 2)
     */
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

    /**
     * This function should be called to update the displayed state (makes a copy)
     * 
     * @param _state information about the new state of the GUI
     */
    public void setState(int[][] _state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = _state[i][j];
            }
        }

        // Tells the system a frame update is required
        repaint();
    }


}
