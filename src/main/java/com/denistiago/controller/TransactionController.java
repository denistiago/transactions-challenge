package com.denistiago.controller;

import com.denistiago.controller.dto.TransactionDTO;
import com.denistiago.domain.Transaction;
import com.denistiago.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated @RequestBody TransactionDTO transactionDTO) {
        LOGGER.info("creating transaction {}", transactionDTO);
        transactionService.create(new Transaction(transactionDTO.getAmount(), transactionDTO.getTimestamp()));
    }

}
