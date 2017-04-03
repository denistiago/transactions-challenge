package com.denistiago.service;

import com.denistiago.domain.Statistics;
import com.denistiago.repository.SlidingTimeStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsService {

    @Autowired
    private SlidingTimeStatisticsRepository repository;

    public Statistics getCurrent() {
        return repository.getStatistics();
    }

}
