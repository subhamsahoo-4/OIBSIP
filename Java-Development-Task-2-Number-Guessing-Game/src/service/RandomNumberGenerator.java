package service;

import java.util.Random;

public class RandomNumberGenerator implements NumberGenerator {
    private final Random random = new Random();

    @Override
    public int generate(int min, int max) {
        if (min > max) throw new IllegalArgumentException("Minimum cannot exceed maximum.");
        return min + random.nextInt(max - min + 1);
    }
}
