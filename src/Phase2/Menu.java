package Phase2;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu {
    private Player player;
    private JFrame frame = new JFrame("Game Menu");
    private JTextField inputField;

    class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                this.backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
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
        frame.setSize(400, 300);

        // Use BorderLayout for the frame's content pane
        frame.setLayout(new BorderLayout());

        // Use the custom BackgroundPanel for the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("src/Phase2/misc/photo.jpg");
        frame.setContentPane(backgroundPanel);
        JPanel buttonPanel = createButtonPanel();
        JPanel labelPanel = createLabelPanel();


        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        backgroundPanel.add(labelPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        
        Color textColour = Color.RED;  
        Icon icon = new ImageIcon("src/Phase2/misc/randomorder.png");      
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
            frame.dispose();
            Tetris tetris = new Tetris();
            tetris.runTetris(); // Start the game loop
            savePlayerInfo();
        });
        buttonPanel.add(randomOrderButton);
        buttonPanel.add(bestOrderButton);
        buttonPanel.add(botButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 75, 50, 75));

        buttonPanel.setOpaque(false);

        return buttonPanel;
    }
     
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private JPanel createLabelPanel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        //labelPanel.setBackground(Color.PINK); Have a talk about this in the meating 

        JPanel labelFieldPanel = new JPanel();
        labelFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //labelFieldPanel.setOpaque(false);
        JLabel inputLabel = new JLabel("Name:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        //inputLabel.setOpaque(false);
        inputLabel.setForeground(Color.black);

        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.BOLD, 16));
        
        labelFieldPanel.add(inputLabel);
        labelFieldPanel.add(inputField);
        labelFieldPanel.setOpaque(false);

        // Create an empty panel for spacing
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(10, 10)); // Adjust the size as needed
        labelPanel.add(spacerPanel, BorderLayout.NORTH);
        labelPanel.add(labelFieldPanel, BorderLayout.CENTER);

        return labelPanel;
    }

    private void savePlayerInfo() {
        String name = inputField.getText();
        player = new Player(name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu();
        });
    }
}