package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.GameRound;

public class ScoreService {
    private final List<String> history = new ArrayList<>();
    private int roundsPlayed;
    private int roundsWon;
    private int attemptsAcrossWins;

    public void recordWin(GameRound round) {
        roundsPlayed++;
        roundsWon++;
        attemptsAcrossWins += round.getAttempts();
        history.add(String.format("Round %d - guessed in %d attempt%s (%s)%n",
                roundsPlayed, round.getAttempts(), round.getAttempts() == 1 ? "" : "s", round.getDifficulty().getName()));
    }

    public void recordLoss(GameRound round) {
        roundsPlayed++;
        history.add(String.format("Round %d - LOST after %d attempts, number was %d (%s)%n",
                roundsPlayed, round.getAttempts(), round.getSecretNumber(), round.getDifficulty().getName()));
    }

    public int getRoundsPlayed() { return roundsPlayed; }
    public int getRoundsWon() { return roundsWon; }
    public double getAverageAttempts() { return roundsWon == 0 ? 0.0 : ((double)  roundsWon/roundsPlayed) * 10.0; }
    public List<String> getHistory() { return Collections.unmodifiableList(history); }

    public void reset() {
        history.clear();
        roundsPlayed = 0;
        roundsWon = 0;
        attemptsAcrossWins = 0;
    }
}
