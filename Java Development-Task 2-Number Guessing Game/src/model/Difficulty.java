package model;

public enum Difficulty {
    BEGINEER("Begineer",1,25,10),
    EASY("Easy", 1, 50, 10),
    MEDIUM("Medium", 1, 100, 9),
    INTERMIDEATE("Intermideate",1,150,8),
    HARD("Hard", 1, 200, 7);

    private final String name;
    private final int min;
    private final int max;
    private final int maxAttempts;

    Difficulty(String name, int min, int max, int maxAttempts) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.maxAttempts = maxAttempts;
    }

    public String getName() { return name; }
    public int getMin() { return min; }
    public int getMax() { return max; }
    public int getMaxAttempts() { return maxAttempts; }

    @Override
    public String toString() {
        return name + " (" + min + "-" + max + ", " + maxAttempts + " attempts)";
    }
}
