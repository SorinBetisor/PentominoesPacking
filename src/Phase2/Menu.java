package Phase2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import Phase2.helperClasses.SoundPlayerUsingClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Menu {
    private static JFrame frame = new JFrame("PENTRIS Menu");
    private SoundPlayerUsingClip soundPlayer;
    private ImageIcon icon;

    class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel()
        {

        }

        public BackgroundPanel(String imagePath) {
            try {
                this.backgroundImage = ImageIO.read(new File(imagePath));
                icon = new ImageIcon( "C:\\Users\\Admin\\Desktop\\Project1-1\\bcs_group_33_project_2023\\src\\Phase2\\misc\\icon.png");
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
        frame.setSize(1050, 600);
        frame.setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "bcs_group_33_project_2023\\src\\Phase2\\misc\\91655.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundPanel);

        // Set the icon
        frame.setIconImage(icon.getImage());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel t1 = new JLabel("",
                new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\Pentris-11-29-2023(1).gif"),
                JLabel.CENTER);
        gbc.insets = new Insets(-5, 0, 20, 0);
        frame.add(t1, gbc);
        t1.setBounds(0, 0, 0, 0);
        

        // Music button
        soundPlayer = new SoundPlayerUsingClip();
        backgroundPanel.add(createButtonPanel(), gbc);
        JButton musicToggle = new JButton();
        musicToggle.setOpaque(false);
        musicToggle.setContentAreaFilled(false);
        musicToggle.setBorderPainted(false);
        musicToggle.setFocusPainted(false);

        // Set the speaker icon (adjust the path accordingly)
        ImageIcon speakerIcon = new ImageIcon("src\\Phase2\\misc\\soundIcon.png");

        // Resize the icon
        Image scaledImage = speakerIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        musicToggle.setIcon(scaledIcon);

        gbc.insets = new Insets(10, 0, 0, 0);
        backgroundPanel.add(musicToggle, gbc);

        frame.setVisible(true);

        Thread musicThread = new Thread(() -> {
            soundPlayer = new SoundPlayerUsingClip();
            playMusic();
        }, "MenuMusicThread");

        musicThread.start();

        musicToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (soundPlayer.isPaused == true) {
                    soundPlayer.resume();
                } else {
                    soundPlayer.pause();
                }
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton randomOrderButton = createButton("PLAY");
        JButton botButton = createButton("Bot");
        JButton bestOrderButton = createButton("Bot Sequence");
        JButton controlsButton = createButton("Controls");

        customizeButton(randomOrderButton, Color.BLACK, Color.BLACK);
        customizeButton(botButton, Color.black, Color.black);
        customizeButton(bestOrderButton, Color.BLACK, Color.BLACK);
        customizeButton(controlsButton, Color.black, Color.BLACK);

        Dimension buttonSize = new Dimension(250, 70); // Set the desired size

        randomOrderButton.setMaximumSize(buttonSize);
        bestOrderButton.setMaximumSize(buttonSize);
        botButton.setMaximumSize(buttonSize);
        controlsButton.setMaximumSize(buttonSize);

        buttonPanel.add(randomOrderButton);
        buttonPanel.add(botButton);

        buttonPanel.add(bestOrderButton);
        buttonPanel.add(controlsButton);

        randomOrderButton.addActionListener(e -> {
            frame.dispose();
            Tetris tetris = new Tetris();
            tetris.runTetris();
        });

        botButton.addActionListener(e -> {
            frame.dispose();
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    Bot bot = new Bot(new double[] { 2.7f, -3.78f, -2.31f, -2.9f, -0.59f, 0.65f, 6.52f, 3.97f });
                    Tetris tetris = bot.tetris;
                    bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
                    return null;
                }
            };
    
            worker.execute();
        });
        

        controlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showControlsDialog();
            }
        });

        bestOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        Tetris.sequence = true;
                        SequenceBot bot = new SequenceBot();
                        Tetris tetris = bot.tetris;
                        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
                        return null;
                    }
                };

                worker.execute();
            }
        });

        return buttonPanel;
    }

    public void showControlsDialog() {
        JDialog controlsDialog = new JDialog(frame, "Game Controls", true);
        controlsDialog.setSize(500, 500);
        controlsDialog.setLocationRelativeTo(frame);

        BackgroundPanel controlsBackground = new BackgroundPanel();
        controlsBackground.setLayout(new BorderLayout());
        controlsDialog.setContentPane(controlsBackground);
        String controlsText = "<html><body style='text-align: center;'>"
                + "<h3 style='font-family: Monospaced;'>Controls:</h3>"
                + "<p style='font-family: Monospaced;'>SPACE - Drops the piece instantly<br>"
                + "LEFT ARROW - Move piece left<br>"
                + "RIGHT ARROW - Move piece right<br>"
                + "UP ARROW - Decrease speed<br>"
                + "DOWN ARROW - Increase speed<br>"
                + "D - Rotate piece clockwise<br>"
                + "A - Rotate piece anticlockwise</p>"
                + "</body></html>";
        
        ImageIcon controlsIcon = new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\Contros__1.jpg");
        JLabel controlsLabel = new JLabel(controlsIcon);
        JLabel textLabel = new JLabel(controlsText,SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Monospaced", Font.ITALIC | Font.BOLD, 18));
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(textLabel, BorderLayout.CENTER);
        controlsLabel.setLayout(new BorderLayout());
        controlsLabel.add(textPanel, BorderLayout.CENTER);        
        controlsLabel.setOpaque(true);
        controlsLabel.setBackground(new Color(128, 0, 158)); // Black background with 150 alpha (transparency)
        controlsBackground.add(controlsLabel);
        controlsDialog.setVisible(true);
        
    
    
    }


    
    
    
    
    private void customizeButton(JButton button, Color foregroundColor, Color borderColor) {
        // Set the background color
        button.setBackground(new Color(128, 0, 128)); // Purple color

        // Set other button properties
        button.setForeground(foregroundColor);
        button.setOpaque(true); // Set opaque to true to show the background color
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 4));
        button.setPreferredSize(new Dimension(250, 80));
        button.setFocusPainted(false);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.LAYOUT_LEFT_TO_RIGHT, 20));
        return button;
    }

    private void playMusic() {
        try {
            soundPlayer.playMusic("src\\Phase2\\misc\\8bit-music-menu8.wav");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu();
        });
    }

}
