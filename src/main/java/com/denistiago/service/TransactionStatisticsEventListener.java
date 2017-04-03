package com.denistiago.service;

import com.denistiago.domain.Transaction;
import com.denistiago.events.TransactionCreatedEvent;
import com.denistiago.repository.SlidingTimeStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionStatisticsEventListener implements ApplicationListener<TransactionCreatedEvent> {

    @Autowired
    private SlidingTimeStatisticsRepository repository;

    @Override
    public void onApplicationEvent(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        repository.add(transaction.getTimestamp(), transaction.getAmount());
    }

}