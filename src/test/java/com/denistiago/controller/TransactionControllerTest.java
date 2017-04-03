package com.denistiago.controller;

import com.denistiago.domain.Transaction;
import com.denistiago.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService service;

    @Test
    public void shouldNotAcceptNegativeAmount() throws Exception {
        this.mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": -2.3, \"timestamp\": 1491202158757}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldExpectTimestamp() throws Exception {
        this.mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldExpectAmount() throws Exception {
        this.mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timestamp\":1491202158757}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAcceptTransaction() throws Exception {
        doNothing().when(service).create(any(Transaction.class));
        this.mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 10.0, \"timestamp\": 1491202158757}"))
                .andExpect(status().isCreated());
    }

}
