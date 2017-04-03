package com.denistiago.repository;

import com.denistiago.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void create(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

}