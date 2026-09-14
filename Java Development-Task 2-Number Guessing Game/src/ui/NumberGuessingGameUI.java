package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Difficulty;
import model.GameResult;
import model.GameRound;
import service.GameService;
import service.ScoreService;

public class NumberGuessingGameUI extends JFrame {
    private final GameService gameService;
    private final ScoreService scoreService;

    private JComboBox<Difficulty> difficultyCombo;
    private JButton startButton;
    private JButton resetStatsButton;
    private JLabel promptLabel;
    private JLabel attemptsLabel;
    private JLabel feedbackLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JTextArea historyArea;
    private JLabel statsLabel;

    public NumberGuessingGameUI(GameService gameService, ScoreService scoreService) {
        super("Number Guessing Game");
        this.gameService = gameService;
        this.scoreService = scoreService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(root);
        root.add(buildDifficultyPanel(), BorderLayout.NORTH);
        root.add(buildGamePanel(), BorderLayout.CENTER);
        root.add(buildHistoryPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildDifficultyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Difficulty"));
        difficultyCombo = new JComboBox<>(Difficulty.values());
        difficultyCombo.setSelectedItem(Difficulty.MEDIUM);
        startButton = new JButton("Start Round");
        startButton.addActionListener(e -> startRound());
        panel.add(new JLabel("Choose difficulty:"));
        panel.add(difficultyCombo);
        panel.add(startButton);
        return panel;
    }

    private JPanel buildGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Game"));
        panel.setPreferredSize(new Dimension(460, 180));

        promptLabel = new JLabel("Click \"Start Round\" to begin.");
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        attemptsLabel = new JLabel(" ");
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guessField = new JTextField(8);
        guessField.setEnabled(false);
        guessButton = new JButton("Guess");
        guessButton.setEnabled(false);
        guessButton.addActionListener(e -> submitGuess());
        guessField.addActionListener(e -> submitGuess());
        inputRow.add(new JLabel("Your guess:"));
        inputRow.add(guessField);
        inputRow.add(guessButton);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setFont(feedbackLabel.getFont().deriveFont(Font.BOLD, 18f));

        panel.add(promptLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(attemptsLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(inputRow);
        panel.add(Box.createVerticalStrut(10));
        panel.add(feedbackLabel);
        return panel;
    }

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Score History"));
        historyArea = new JTextArea(8, 48);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        statsLabel = new JLabel("Rounds played: 0 | Rounds won: 0 | Avg attempts (wins): 0.0");
        resetStatsButton = new JButton("Reset Stats");
        resetStatsButton.addActionListener(e -> resetStats());
        bottom.add(statsLabel, BorderLayout.CENTER);
        bottom.add(resetStatsButton, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void startRound() {
        Difficulty difficulty = (Difficulty) difficultyCombo.getSelectedItem();
        gameService.startRound(difficulty);
        GameRound round = gameService.getCurrentRound();
        promptLabel.setText("Round " + (scoreService.getRoundsPlayed() + 1) + ": Guess a number between "
                + difficulty.getMin() + " and " + difficulty.getMax());
        attemptsLabel.setText("Attempts: 0 / " + round.getMaxAttempts());
        feedbackLabel.setText(" ");
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        difficultyCombo.setEnabled(false);
        startButton.setEnabled(false);
        resetStatsButton.setEnabled(false);
        guessField.requestFocusInWindow();
    }

    private void submitGuess() {
        if (!gameService.isRoundActive()) return;
        int guess;
        try {
            guess = Integer.parseInt(guessField.getText().trim());
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Please enter a whole number.");
            return;
        }

        GameResult result;
        try {
            result = gameService.makeGuess(guess);
        } catch (IllegalArgumentException | IllegalStateException e) {
            feedbackLabel.setText(e.getMessage());
            return;
        }

        GameRound round = gameService.getCurrentRound();
        attemptsLabel.setText("Attempts: " + round.getAttempts() + " / " + round.getMaxAttempts());
        guessField.setText("");

        if (result == GameResult.CORRECT) {
            feedbackLabel.setText("Correct! The number was " + round.getSecretNumber() + "!");
            endRound(true);
        } else if (result == GameResult.LOST) {
            feedbackLabel.setText("You Lost! The number was " + round.getSecretNumber() + ".");
            endRound(false);
        } else {
            feedbackLabel.setText(result.getMessage());
            guessField.requestFocusInWindow();
        }
    }

    private void endRound(boolean won) {
        GameRound round = gameService.getCurrentRound();
        if (won) scoreService.recordWin(round); 
        else scoreService.recordLoss(round);
        updateScoreDisplay();
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        difficultyCombo.setEnabled(true);
        startButton.setEnabled(true);
        resetStatsButton.setEnabled(true);
        playAgain();
    }

    private void playAgain() {
        int choice = JOptionPane.showConfirmDialog(this, "Play again?", "Round Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) startRound();
        else promptLabel.setText("Thanks for playing! Pick a difficulty and press \"Start Round\" any time.");
    }

    private void updateScoreDisplay() {
        historyArea.setText("");
        for (String entry : scoreService.getHistory()) historyArea.append(entry);
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
        statsLabel.setText(String.format("Rounds played: %d | Rounds won: %d | Avg attempts (wins): %.1f",
                scoreService.getRoundsPlayed(), scoreService.getRoundsWon(), scoreService.getAverageAttempts()));
    }

    private void resetStats() {
        scoreService.reset();
        historyArea.setText("");
        statsLabel.setText("Rounds played: 0 | Rounds won: 0 | Avg attempts (wins): 0.0");
        promptLabel.setText("Stats reset. Click \"Start Round\" to begin a new game.");
        feedbackLabel.setText(" ");
        attemptsLabel.setText(" ");
    }
}
