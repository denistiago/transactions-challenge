package com.denistiago.events;

import com.denistiago.domain.Transaction;
import org.springframework.context.ApplicationEvent;

public class TransactionCreatedEvent extends ApplicationEvent {

    private Transaction transaction;

    public TransactionCreatedEvent(Transaction source) {
        super(source);
        this.transaction = source;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
