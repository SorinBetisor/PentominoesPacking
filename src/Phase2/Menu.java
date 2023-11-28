package Phase2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import Phase2.helperClasses.SoundPlayerUsingClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Menu {


    //To Add : change sound icon, use sound sliders, add title, add name text box (back).
    private Player player;
    private static JFrame frame = new JFrame("PENTRIS Menu");
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

    private JLabel createTitleLabel(String text) {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(128, 0, 128));
        return titleLabel;
    }


    public Menu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/Phase2/misc/91655.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        Color randomColor = new Color(red, green, blue);



        JLabel titleLabel = createTitleLabel("PENTRIS");
        gbc.insets = new Insets(20, 0, 20, 0);
        titleLabel.setBackground(randomColor);
        backgroundPanel.add(titleLabel, gbc);


        backgroundPanel.add(createButtonPanel(), gbc);


        musicToggle = new JCheckBox("\uD83D\uDD0A"); //need to change
        musicToggle.setSelected(true);
        musicToggle.setOpaque(false);
        musicToggle.setContentAreaFilled(false);
        musicToggle.addActionListener(e -> toggleMusic());
        gbc.insets = new Insets(10, 0, 0, 0);
        backgroundPanel.add(musicToggle, gbc);


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
        buttonPanel.setOpaque(false);

        JButton randomOrderButton = createButton("PLAY");
        JButton bestOrderButton = createButton("Bot - Best Order");
        JButton botButton = createButton("Bot");

        JButton controlsButton = new JButton("Controls");
        controlsButton.setFont(new Font("Arial", Font.BOLD, 14));


        customizeButton(randomOrderButton, Color.pink, Color.pink);
        customizeButton(bestOrderButton, Color.GREEN, Color.green);
        customizeButton(botButton, Color.white, Color.white);
        customizeButton(controlsButton,Color.ORANGE,Color.ORANGE);

        buttonPanel.add(randomOrderButton);
        buttonPanel.add(bestOrderButton);
        buttonPanel.add(botButton);
        buttonPanel.add(controlsButton);


        randomOrderButton.addActionListener(e -> {
            //System.out.println("Random Order Button Clicked");
            //toggleMusic();
            frame.dispose();
            Tetris tetris = new Tetris();
            tetris.runTetris();
            savePlayerInfo();
        });

        controlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showControlsDialog();
            }
        });

        //4 commit
        return buttonPanel;
    }

    public void showControlsDialog(){

        JDialog controlsDialog = new JDialog(frame, "Game Controls", true);
        controlsDialog.setSize(400, 300);
        controlsDialog.setLocationRelativeTo(frame);


        BackgroundPanel controlsBackground = new BackgroundPanel("src/Phase2/misc/photo.jpg");
        controlsBackground.setLayout(new BorderLayout());
        controlsDialog.setContentPane(controlsBackground);


        String controlsText = "<html><body style='text-align: center;'>"
                + "<h3>Controls:</h3>"
                + "SPACE - Drops the piece instantly<br>"
                + "LEFT ARROW - Move piece left<br>"
                + "RIGHT ARROW - Move piece right<br>"
                + "UP ARROW - Decrease speed<br>"
                + "DOWN ARROW - Increase speed<br>"
                + "D - Rotate piece clockwise<br>"
                + "A - Rotate piece anticlockwise"
                + "</body></html>";


        JLabel controlsLabel = new JLabel(controlsText, SwingConstants.CENTER);
        controlsLabel.setForeground(Color.WHITE);
        controlsBackground.add(controlsLabel);

        // Display the dialog
        controlsDialog.setVisible(true);
    }



    private void customizeButton(JButton button, Color foregroundColor, Color borderColor) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(foregroundColor);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 4));
        button.setPreferredSize(new Dimension(200, 50));


    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
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
