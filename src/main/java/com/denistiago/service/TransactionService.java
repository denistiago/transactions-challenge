package com.denistiago.service;

import com.denistiago.domain.Transaction;
import com.denistiago.events.TransactionCreatedEvent;
import com.denistiago.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

    private ApplicationEventPublisher publisher;
    private TransactionRepository repository;

    @Autowired
    public TransactionService(ApplicationEventPublisher publisher,
                              TransactionRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    public void create(Transaction transaction) {
        repository.create(transaction);
        publisher.publishEvent(new TransactionCreatedEvent(transaction));
    }

}