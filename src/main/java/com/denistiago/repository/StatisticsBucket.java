package com.denistiago.repository;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * structure to support moving average
 */
class StatisticsBucket {

    private AtomicLong count = new AtomicLong();
    private DoubleAdder amount = new DoubleAdder();
    private Double min;
    private Double max;

    private Long latestAddedTimestamp;

    /**
     * only one thread at time writing to the same bucket
     */
    synchronized void add(Double amount, long timestamp) {
        this.count.incrementAndGet();
        this.amount.add(amount);
        this.latestAddedTimestamp = timestamp;
        addMinIfLess(amount);
        addMaxIfGreaterThan(amount);
    }

    private void addMaxIfGreaterThan(Double amount) {
        if (max == null)
            max = amount;
        if (amount > max) {
            max = amount;
        }
    }

    private void addMinIfLess(Double amount) {
        if (min == null)
            min = amount;
        if (amount < min)
            min = amount;
    }

    synchronized void reset() {
        this.count = new AtomicLong();
        this.amount = new DoubleAdder();
        this.min = null;
        this.max = null;
    }

    Long getLatestAddedTimestamp() {
        return latestAddedTimestamp;
    }

    boolean isNotEmpty() {
        return count.get() > 0;
    }

    Long getCount() {
        return count.get();
    }


    Double getAmount() {
        return amount.sum();
    }

    Double getMin() {
        return min;
    }

    Double getMax() {
        return max;
    }

}
