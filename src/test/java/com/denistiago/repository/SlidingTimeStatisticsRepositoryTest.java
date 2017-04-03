package com.denistiago.repository;

import com.denistiago.domain.Statistics;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SlidingTimeStatisticsRepositoryTest {

    private Clock clock;
    private SlidingTimeStatisticsRepository repository;
    private ExecutorService executorService;

    @Before
    public void before() {
        this.clock = Clock.systemDefaultZone();
        this.repository = new SlidingTimeStatisticsRepository();
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Test
    public void testStatisticsCalculation() {

        repository.add(clock.millis(), 10);
        repository.add(clock.millis(), 5);
        repository.add(clock.millis(), 3);

        Statistics statistics = repository.getStatistics();

        assertStatistics(statistics, 3L, 3.0, 10.0, 6.0, 18.0);
    }

    @Test
    public void shouldCalculateOnlyLatest60Seconds() {

        repository.add(clock.millis(), 10);
        assertStatistics(repository.getStatistics(), 1L, 10.0, 10.0, 10.0, 10.0);

        elapseDuration(Duration.ofSeconds(60));
        repository.add(clock.millis(), 5);
        assertStatistics(repository.getStatistics(), 1L, 5.0, 5.0, 5.0, 5.0);

        elapseDuration(Duration.ofSeconds(10));
        repository.add(clock.millis(), 7);
        assertStatistics(repository.getStatistics(), 2L, 5.0, 7.0, 6.0, 12.0);

        elapseDuration(Duration.ofSeconds(50));
        repository.add(clock.millis(), 7);
        assertStatistics(repository.getStatistics(), 2L, 7.0, 7.0, 7.0, 14.0);

    }

    @Test
    public void shouldCalculateWithMultiThreads() throws InterruptedException {

        final List<Double> amounts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            double n = new Random().nextInt();
            amounts.add(n);
            executorService.submit(() -> repository.add(clock.millis(), n));
        }

        waitThreadsTermination(executorService);
        assertStatisticsEqualsAmountsSummaryStatistics(amounts);

    }

    @Test
    public void shouldCalculateWithMultiThreadsInTheSameTimestamp() throws InterruptedException {

        final List<Double> amounts = new ArrayList<>();
        final long timestamp = clock.millis();
        for (int i = 0; i < 100; i++) {
            double n = new Random().nextInt();
            amounts.add(n);
            executorService.submit(() -> repository.add(timestamp, n));
        }

        waitThreadsTermination(executorService);
        assertStatisticsEqualsAmountsSummaryStatistics(amounts);

    }

    @Test
    public void shouldResetOutOfRangeBuckets() {

        repository.add(clock.millis(), 10);
        elapseDuration(Duration.ofSeconds(61));
        assertThat(0L, Matchers.equalTo(repository.getStatistics().getCount()));

    }

    @Test
    public void shouldAllowTimestampsInPast() {

        repository.add(Clock.offset(clock, Duration.ofSeconds(-10)).millis(), 10);
        assertThat(1L, Matchers.equalTo(repository.getStatistics().getCount()));

    }

    @Test
    public void timestampsInTheFutureShouldBeConsideredNow() {
        repository.add(Clock.offset(clock, Duration.ofSeconds(70)).millis(), 10);
        assertThat(1L, Matchers.equalTo(repository.getStatistics().getCount()));
    }

    private void waitThreadsTermination(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    private void assertStatisticsEqualsAmountsSummaryStatistics(List<Double> amounts) {
        DoubleSummaryStatistics expected = amounts.stream().mapToDouble(d -> d).summaryStatistics();
        Statistics actual = repository.getStatistics();
        assertThat(expected.getCount(), Matchers.equalTo(actual.getCount()));
        assertThat(expected.getSum(), Matchers.equalTo(actual.getSum()));
        assertThat(expected.getMin(), Matchers.equalTo(actual.getMin()));
        assertThat(expected.getMax(), Matchers.equalTo(actual.getMax()));
        assertThat(expected.getAverage(), Matchers.equalTo(actual.getAverage()));
    }

    private void elapseDuration(Duration duration) {
        Clock offset = Clock.offset(clock, duration);
        repository.setClock(offset);
        clock = offset;
    }

    private void assertStatistics(Statistics statistics, long count, double min, double max, double avg, double sum) {
        assertThat(statistics.getCount(), equalTo(count));
        assertThat(statistics.getMin(), equalTo(min));
        assertThat(statistics.getMax(), equalTo(max));
        assertThat(statistics.getAverage(), equalTo(avg));
        assertThat(statistics.getSum(), equalTo(sum));
    }


}
