package com.denistiago.controller;

import com.denistiago.domain.Statistics;
import com.denistiago.service.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService service;

    @Test
    public void testGetEmptyStatistics() throws Exception {
        when(service.getCurrent()).thenReturn(Statistics.empty());
        this.mvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\":0.0,\"average\":0.0,\"min\":0.0,\"max\":0.0,\"count\":0}"));
    }

}
