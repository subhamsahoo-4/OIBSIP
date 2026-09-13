package model;

public class GameRound {
    private final Difficulty difficulty;
    private final int secretNumber;
    private int attempts;
    private boolean active;

    public GameRound(Difficulty difficulty, int secretNumber) {
        this.difficulty = difficulty;
        this.secretNumber = secretNumber;
        this.active = true;
    }

    public GameResult makeGuess(int guess) {
        if (!active) throw new IllegalStateException("The round is over.");
        if (guess < difficulty.getMin() || guess > difficulty.getMax()) {
            throw new IllegalArgumentException("Enter a number between " + difficulty.getMin() + " and " + difficulty.getMax() + ".");
        }
        attempts++;
        if (guess == secretNumber) { active = false; return GameResult.CORRECT; }
        if (attempts >= difficulty.getMaxAttempts()) { active = false; return GameResult.LOST; }
        return guess > secretNumber ? GameResult.TOO_HIGH : GameResult.TOO_LOW;
    }

    public Difficulty getDifficulty() { return difficulty; }
    public int getAttempts() { return attempts; }
    public int getMaxAttempts() { return difficulty.getMaxAttempts(); }
    public int getSecretNumber() { return secretNumber; }
    public boolean isActive() { return active; }
}
