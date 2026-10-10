package model;

public enum GameResult {
    TOO_HIGH("Too High!"),
    TOO_LOW("Too Low!"),
    CORRECT("Correct!"),
    LOST("You Lost!");

    private final String message;

    GameResult(String message) { this.message = message; }
    public String getMessage() { return message; }
}
