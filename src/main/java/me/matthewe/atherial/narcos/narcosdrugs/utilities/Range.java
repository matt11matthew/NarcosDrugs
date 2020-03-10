package me.matthewe.atherial.narcos.narcosdrugs.utilities;

public abstract class Range<T extends Number> {
    protected  T min;
    protected T max;

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public abstract T getRandomNumberInRange();

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    @Override
    public String toString() {
        return min + "-" + max;
    }
}
