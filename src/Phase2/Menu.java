/*
package Phase2;

import Phase2.helperClasses.SoundPlayerUsingClip;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

*
 * The Menu class represents the main menu of the PENTRIS game.
 * It provides a graphical user interface with various buttons and options.
 * The menu allows the user to start the game, control the music, and access
 * game controls.


public class Menu {
    private static JFrame frame = new JFrame("PENTRIS Menu");
    private SoundPlayerUsingClip soundPlayer;
    private ImageIcon icon;

*
     * A custom JPanel class that displays a background image.


    class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

*
         * Constructs a new BackgroundPanel with no background image.


        public BackgroundPanel() {

        }

*
         * Constructs a new BackgroundPanel with the specified background image.
         *
         * @param imagePath the path to the background image file


        public BackgroundPanel(String imagePath) {
            try {
                this.backgroundImage = ImageIO.read(new File(imagePath));
                if (MainScreen.fileExists("src/Phase2/misc/icon.png")) {
                    icon = new ImageIcon("src/Phase2/misc/icon.png");
                } else if (MainScreen.fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\icon.png")) {
                    icon = new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\icon.png");
                } else {
                    System.out.println("Both paths do not exist.");
                }
                this.addKeyListener(new MyKeyListener());
                this.setFocusable(true);
                this.requestFocusInWindow();
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

*
     * Constructs a Menu object.
     * Initializes the frame, sets its size and location, and sets the default close
     * operation.
     * Creates a background panel and sets it as the content pane of the frame.
     * Sets the icon image of the frame.
     * Creates a label with an image icon and adds it to the frame.
     * Creates a music toggle button with a speaker icon and adds it to the
     * background panel.
     * Starts a new thread to play background music.
     * Adds an action listener to the music toggle button to pause or resume the
     * music.


    public Menu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 600);
        frame.setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = null;
        if (MainScreen.fileExists("src/Phase2/misc/91655.jpg")) {
            backgroundPanel = new BackgroundPanel(
                    "src/Phase2/misc/91655.jpg");
        } else if (MainScreen.fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\91655.jpg")) {
            backgroundPanel = new BackgroundPanel(
                    "bcs_group_33_project_2023\\src\\Phase2\\misc\\91655.jpg");
        } else {
            System.out.println("Both paths do not exist.");
        }

        backgroundPanel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundPanel);

        // Set the icon
        frame.setIconImage(icon.getImage());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel t1 = null;

        if (MainScreen.fileExists("src/Phase2/misc/Pentris-11-29-2023(1).gif")) {
            t1 = new JLabel("",
                    new ImageIcon("src/Phase2/misc/Pentris-11-29-2023(1).gif"),
                    JLabel.CENTER);
        } else if (MainScreen.fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\Pentris-11-29-2023(1).gif")) {
            t1 = new JLabel("",
                    new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\Pentris-11-29-2023(1).gif"),
                    JLabel.CENTER);
        } else {
            System.out.println("Both paths do not exist.");
        }
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
        ImageIcon speakerIcon = null;

        if (MainScreen.fileExists("src/Phase2/misc/soundIcon.png")) {
            speakerIcon = new ImageIcon("src/Phase2/misc/soundIcon.png");
        } else if (MainScreen.fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\soundIcon.png")) {
            speakerIcon = new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\soundIcon.png");
        }

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

*
     * Creates a JPanel for the button panel.
     * 
     * @return the created JPanel


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
                    Bot bot = new Bot(new double[] { -3.71, 3.7, -4.79, -2.9, 4.98, 3.22, 4.8 });
                    Tetris tetris = bot.tetris;
                    bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE,
                            Tetris.VERTICAL_GRID_SIZE);
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
                        bot.runBot(tetris.field, tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE,
                                Tetris.VERTICAL_GRID_SIZE);
                        return null;
                    }
                };

                worker.execute();
            }
        });

        return buttonPanel;
    }

    class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            // Handle key typed event (not used in this example)
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // Handle key pressed event
            if (e.getKeyChar() == 'o' || e.getKeyChar() == 'O' || e.getKeyChar() == '0') {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Tetris.sequence = true;
                        frame.dispose();
                        SemiRandomBot bot = new SemiRandomBot();
                        bot.runBot(bot.workingField, bot.tetris.currentPiece, Tetris.HORIZONTAL_GRID_SIZE, Tetris.VERTICAL_GRID_SIZE);
                        return null;
                    }
                };
                worker.execute();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // Handle key released event (not used in this example)
        }
    }

*
     * Displays a dialog box with game controls information.


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

        ImageIcon controlsIcon = new ImageIcon("src/Phase2/misc/Contros__1.jpg");

        if (MainScreen.fileExists("src/Phase2/misc/Contros__1.jpg")) {
            controlsIcon = new ImageIcon("src/Phase2/misc/Contros__1.jpg");
        } else if (MainScreen
                .fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\Contros__1.jpg")) {
            controlsIcon = new ImageIcon("bcs_group_33_project_2023\\src\\Phase2\\misc\\Contros__1.jpg");
        }
        JLabel controlsLabel = new JLabel(controlsIcon);
        JLabel textLabel = new JLabel(controlsText, SwingConstants.CENTER);
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

*
     * Customizes a JButton by setting its background color, foreground color, and
     * border color.
     * 
     * @param button          the JButton to be customized
     * @param foregroundColor the foreground color of the button
     * @param borderColor     the border color of the button


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

*
     * Creates a JButton with the specified text.
     *
     * @param text the text to be displayed on the button
     * @return the created JButton


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.LAYOUT_LEFT_TO_RIGHT, 20));
        return button;
    }

*
     * Plays the menu music.
     * If the music file exists in the specified paths, it will be played using the
     * soundPlayer.
     * If the file does not exist, the method will return without playing any music.
     * 
     * @throws LineUnavailableException if the audio line is unavailable


    private void playMusic() {
        try {

            if (MainScreen.fileExists("src/Phase2/misc/8bit-music-menu8.wav")) {
                soundPlayer.playMusic("src/Phase2/misc/8bit-music-menu8.wav");
            } else if (MainScreen
                    .fileExists("bcs_group_33_project_2023\\src\\Phase2\\misc\\8bit-music-menu8.wav")) {
                soundPlayer.playMusic("bcs_group_33_project_2023\\src\\Phase2\\misc\\8bit-music-menu8.wav");
            } else {
                return;
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

*
     * The main method is the entry point of the program.
     * It initializes the SwingUtilities and creates a new instance of the Menu
     * class.


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu();
        });

    }
  

}
*/
