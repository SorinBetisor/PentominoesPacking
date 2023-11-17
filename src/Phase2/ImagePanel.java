package Phase2;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A custom JPanel that displays an image as its background.
 * The image is loaded from the given file path.
 */
public class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;

    /**
     * Constructs an ImagePanel with the given image file path.
     * @param imagePath the file path of the image to be displayed as the background
     */
    public ImagePanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Paints the background image on the panel.
     * @param g the Graphics object used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}