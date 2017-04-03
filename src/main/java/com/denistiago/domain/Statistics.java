package com.denistiago.domain;

public class Statistics {

    private final double sum;
    private final double average;
    private final double min;
    private final double max;
    private final long count;

    public Statistics(double sum, double avg, double min, double max, long count) {
        this.sum = sum;
        this.average = avg;
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public static Statistics empty() {
        return new Statistics(0d, 0d, 0d, 0d, 0);
    }

    public double getSum() {
        return sum;
    }

    public double getAverage() {
        return average;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "sum=" + sum +
                ", average=" + average +
                ", min=" + min +
                ", max=" + max +
                ", count=" + count +
                '}';
    }
}
