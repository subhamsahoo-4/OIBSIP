package service;

import model.Difficulty;
import model.GameResult;
import model.GameRound;

public class GameService {
    private final NumberGenerator numberGenerator;
    private GameRound currentRound;

    public GameService(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public void startRound(Difficulty difficulty) {
        if (difficulty == null) throw new IllegalArgumentException("Difficulty is required.");
        int secret = numberGenerator.generate(difficulty.getMin(), difficulty.getMax());
        currentRound = new GameRound(difficulty, secret);
    }

    public GameResult makeGuess(int guess) {
        if (currentRound == null) throw new IllegalStateException("No round has been started.");
        return currentRound.makeGuess(guess);
    }

    public GameRound getCurrentRound() { return currentRound; }
    public boolean isRoundActive() { return currentRound != null && currentRound.isActive(); }
}
