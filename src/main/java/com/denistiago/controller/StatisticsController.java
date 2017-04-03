package com.denistiago.controller;


import com.denistiago.domain.Statistics;
import com.denistiago.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(method = RequestMethod.GET)
    public Statistics get() {
        LOGGER.info("getting statistics");
        return statisticsService.getCurrent();
    }

}
