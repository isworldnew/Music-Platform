package ru.smirnov.demandservice.util;

import java.util.List;
import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();

    // предварительно нужно проверить, что values - не null и не пустой
    public static int getRandomIndex(List<Long> values) {
        return random.nextInt(values.size());
    }

}
