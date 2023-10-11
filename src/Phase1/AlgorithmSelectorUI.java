package Phase1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlgorithmSelectorUI {

    public AlgorithmSelectorUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Algorithm Selector");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel panel = new JPanel();

                JLabel algorithmLabel = new JLabel("Select Algorithm:");
                panel.add(algorithmLabel);

                String[] algorithms = {"Random Search (unreliable)", "Pruned Brute Force", "Dancing Links AlgX Search", "Flood Fill"};
                JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
                panel.add(algorithmComboBox);

                JButton startButton = new JButton("Start");
                panel.add(startButton);

                startButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedAlgorithmIndex = algorithmComboBox.getSelectedIndex() + 1;
                        // Start the algorithm on a new thread
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Search.selectAlgorithm(selectedAlgorithmIndex, Search.field);
                            }
                        }).start();
                    }
                });

                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new AlgorithmSelectorUI();
        // Create and display the second UI in real-time on a separate EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // createAndShowSecondUI();
            }
        });
    }

}
