import service.GameService;
import service.NumberGenerator;
import service.RandomNumberGenerator;
import service.ScoreService;
import ui.NumberGuessingGameUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        NumberGenerator generator = new RandomNumberGenerator();
        GameService gameService = new GameService(generator);
        ScoreService scoreService = new ScoreService();

        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameUI game = new NumberGuessingGameUI(gameService, scoreService);
            game.setVisible(true);
        });
    }
}
