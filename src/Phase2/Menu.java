package Phase2;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import Phase2.helperClasses.SoundPlayerUsingClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Menu {
    private Player player;
    private JFrame frame = new JFrame("Game Menu");
    private JTextField inputField;
    private SoundPlayerUsingClip soundPlayer;
    private JCheckBox musicToggle;

    class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                this.backgroundImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public Menu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        BackgroundPanel backgroundPanel = new BackgroundPanel("src/Phase2/misc/91655.jpg");
        frame.setContentPane(backgroundPanel);
        JPanel buttonPanel = createButtonPanel();
        JPanel labelPanel = createLabelPanel();


        musicToggle = new JCheckBox("Toggle Music");
        musicToggle.setSelected(true);
        musicToggle.addActionListener(e -> toggleMusic());

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        backgroundPanel.add(labelPanel, BorderLayout.NORTH);
        backgroundPanel.add(musicToggle, BorderLayout.SOUTH);
        frame.setVisible(true);


        Thread musicThread = new Thread(() -> {
            soundPlayer = new SoundPlayerUsingClip();
            playMusic();
        }, "MenuMusicThread");

        musicThread.start();
    }

  private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton randomOrderButton = createButton("Random Order");
        JButton bestOrderButton = createButton("Best Order");
        JButton botButton = createButton("Bot");

        randomOrderButton.setBackground(new Color(59, 89, 182));
        randomOrderButton.setForeground(Color.pink);
        randomOrderButton.setOpaque(false);
        randomOrderButton.setContentAreaFilled(true);
        randomOrderButton.setBorderPainted(true);
        randomOrderButton.setBorder(BorderFactory.createLineBorder(Color.pink, 4));

        bestOrderButton.setBackground(new Color(59, 89, 182));
        bestOrderButton.setForeground(Color.GREEN);
        bestOrderButton.setOpaque(false);
        bestOrderButton.setContentAreaFilled(false);
        bestOrderButton.setBorderPainted(true);
        bestOrderButton.setBorder(BorderFactory.createLineBorder(Color.green, 4));

        botButton.setBackground(new Color(59, 89, 182));
        botButton.setForeground(Color.BLUE);
        botButton.setOpaque(false);
        botButton.setContentAreaFilled(false);
        botButton.setBorderPainted(true);
        botButton.setBorder(BorderFactory.createLineBorder(Color.blue, 4));

        randomOrderButton.addActionListener(e -> {
            System.out.println("Random Order Button Clicked");
            toggleMusic();
            frame.dispose();
            Tetris tetris = new Tetris();
            tetris.runTetris();
            savePlayerInfo();
        });
        buttonPanel.add(randomOrderButton);
        buttonPanel.add(bestOrderButton);
        buttonPanel.add(botButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 75, 50, 75));
        buttonPanel.setOpaque(false);

        return buttonPanel;
    }


    private void customizeButton(JButton button, Color foregroundColor, Color borderColor) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(foregroundColor);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 4));
        button.setPreferredSize(new Dimension(200, 50)); // Set a preferred size for bigger buttons
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private JPanel createLabelPanel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());

        JPanel labelFieldPanel = new JPanel();
        labelFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel inputLabel = new JLabel("Name:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputLabel.setForeground(Color.black);

        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.BOLD, 16));

        labelFieldPanel.add(inputLabel);
        labelFieldPanel.add(inputField);
        labelFieldPanel.setOpaque(false);

        //spacing.
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(10, 10));
        labelPanel.add(spacerPanel, BorderLayout.NORTH);
        labelPanel.add(labelFieldPanel, BorderLayout.CENTER);

        return labelPanel;
    }

    private void savePlayerInfo() {
        String name = inputField.getText();
        player = new Player(name);
    }



    private void playMusic() {
        Thread musicThread = new Thread(() -> {
            try {
                soundPlayer = new SoundPlayerUsingClip();
                soundPlayer.playMusic("Phase2/misc/8bit-music-menu8.wav");
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }, "MenuMusicThread");

        musicThread.start();
    }

    private void stopMusic() {
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
    }

    private void toggleMusic() {
        if (musicToggle.isSelected()) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu();
        });
    }
}
