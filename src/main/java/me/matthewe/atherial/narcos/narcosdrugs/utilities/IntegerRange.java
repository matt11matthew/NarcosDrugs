package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import java.util.Random;

public class IntegerRange extends Range<Integer> {
    public IntegerRange(Integer min, Integer max) {
        super(min, max);
    }

    public IntegerRange(String input) {
        super(0, 0);
        if (input.contains("-")) {
            this.min = Integer.valueOf(input.split("-")[0].trim());
            this.max = Integer.valueOf(input.split("-")[1].trim());
        } else {
            this.min = Integer.valueOf(input.trim());
            this.max = min;
        }
    }

    @Override
    public Integer getRandomNumberInRange() {
        if (min >= max) {
            return min;
        }
        return new Random().nextInt((max - min) + 1) + min;
    }
}
