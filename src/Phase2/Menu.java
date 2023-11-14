package Phase2;

import javax.swing.*;
import java.awt.*;

public class Menu {
    private Player player;
    JFrame frame = new JFrame("Game Menu");
    JTextField inputField;

    public Menu() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = createButtonPanel();
        JPanel labelPanel = createLabelPanel();

        JFrame frame = new JFrame("Background Image Example");
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\Armantos\\Desktop\\Uni staff\\Lab Games\\Lab Game 2\\Product\\src\\photo.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(backgroundLabel);
        frame.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(labelPanel, BorderLayout.NORTH);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton randomOrderButton = createButton("Random Order");
        JButton bestOrderButton = createButton("Best Order");
        JButton botButton = createButton("Bot");

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

        JPanel labelFieldPanel = new JPanel();
        labelFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel inputLabel = new JLabel("Name:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 16));

        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.BOLD, 16));

        labelFieldPanel.add(inputLabel);
        labelFieldPanel.add(inputField);

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
