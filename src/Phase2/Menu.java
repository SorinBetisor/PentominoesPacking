package Phase2;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Menu menu = new Menu();

    }
    private Player player;
    public Menu(){
        System.out.println("Please enter your name");
        Scanner scanner = new Scanner(System.in);
        Player player = new Player(scanner.nextLine(), 0);

        JFrame frame = new JFrame("Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton randomOrderButton = new JButton("Random Order");
        randomOrderButton.setFont(new Font("Arial", Font.BOLD, 16));
        JButton bestOrderButton = new JButton("Best Order");
        bestOrderButton.setFont(new Font("Arial", Font.BOLD, 16));
        JButton botButton = new JButton("Bot");
        botButton.setFont(new Font("Arial", Font.BOLD, 16));

        buttonPanel.add(randomOrderButton);
        buttonPanel.add(bestOrderButton);
        buttonPanel.add(botButton);

       /* JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));*/


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 75, 50, 75));

        frame.add(buttonPanel, BorderLayout.CENTER);
        //frame.add(startButton, BorderLayout.SOUTH);
        frame.setVisible(true);

    }
}



