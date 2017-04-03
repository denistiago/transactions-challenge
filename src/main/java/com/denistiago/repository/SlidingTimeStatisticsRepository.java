package com.denistiago.repository;

import com.denistiago.domain.Statistics;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The final solution is a mix of Circular Ring Buffer and Moving Average algorithms
 */
@Component
public class SlidingTimeStatisticsRepository {

    private final int WINDOW_SIZE_IN_SECONDS = 60;

    private Long latestAddedTimestamp;
    private Clock clock = Clock.systemDefaultZone();
    private StatisticsBucket[] secondBuckets;

    /**
     * Initializing all buckets at initialization to avoid synchronization on add operation
     */
    public SlidingTimeStatisticsRepository() {
        this.secondBuckets = new StatisticsBucket[WINDOW_SIZE_IN_SECONDS];
        for (int i = 0; i < secondBuckets.length; i++) {
            secondBuckets[i] = new StatisticsBucket();
        }
    }

    /**
     * add amount to the to it's currentTimestampIfProvidedInFuture second bucket
     */
    public void add(long timestamp, double amount) {

        timestamp = currentTimestampIfProvidedInFuture(timestamp);

        resetOutOfRangeBuckets();

        if(inWindowRange(timestamp)) {

            int second = timestampSecond(timestamp);
            secondBuckets[second].add(amount, timestamp);

            if (latestAddedTimestamp == null || timestamp > latestAddedTimestamp)
                latestAddedTimestamp = timestamp;
        }

    }

    /**
     * it was not mentioned on the requirements what should we do with timestamps in the future.
     * so, timestamps in the future will be replaced by current clock millis
     */
    private long currentTimestampIfProvidedInFuture(long timestamp) {
        return Math.min(timestamp, clock.millis());
    }

    public Statistics getStatistics() {
        resetOutOfRangeBuckets();
        return computeStatistics();
    }

    private int timestampSecond(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()).getSecond();
    }

    /**
     * compute statistics of latest 60 seconds buckets
     */
    private Statistics computeStatistics() {

        double max = 0;
        double min = 0;
        double amount = 0d;
        long count = 0;
        double avg = 0.0;

        for (StatisticsBucket bucket : secondBuckets) {

            if (bucket.isNotEmpty()) {

                if (max == 0 || bucket.getMax() > max) {
                    max = bucket.getMax();
                }

                if (min == 0 || bucket.getMin() < min) {
                    min = bucket.getMin();
                }

                amount += bucket.getAmount();
                count += bucket.getCount();
            }

        }

        if (count > 0) {
            avg = amount / count;
        }

        return new Statistics(amount, avg, min, max, count);
    }

    /**
     * check if currentTimestampIfProvidedInFuture is in a valid window range (in now() - 60 seconds interval)
     */
    private boolean inWindowRange(long timestamp) {
        long latestTimestampPossible = Instant.now(clock).minusSeconds(WINDOW_SIZE_IN_SECONDS).getEpochSecond();
        long timestampInSeconds = Instant.ofEpochMilli(timestamp).getEpochSecond();
        return timestampInSeconds > latestTimestampPossible;
    }

    /**
     * resetting buckets that are out of range
     *
     * if latestAddedTimestamp is null we have no buckets to reset
     *
     * if latestAddedTimestamp is more than 60 seconds away we just reset all buckets
     *
     * if latestAddedTimestamp is in window range we reset just buckets out of range
     *
     */
    private void resetOutOfRangeBuckets() {

        if (latestAddedTimestamp == null)
            return;

        if (inWindowRange(latestAddedTimestamp)) {

            for (StatisticsBucket bucket : secondBuckets) {
                if (bucket.isNotEmpty() && !inWindowRange(bucket.getLatestAddedTimestamp())) {
                    bucket.reset();
                }
            }

        } else {

            for (StatisticsBucket bucket : secondBuckets) {
                bucket.reset();
            }
        }
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }

}

