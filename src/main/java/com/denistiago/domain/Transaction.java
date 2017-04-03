package com.denistiago.domain;

import java.util.Date;
import java.util.UUID;

public class Transaction {

    private String id;
    private Double amount;
    private long timestamp;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
    }

    public Transaction(Double amount, long timestamp) {
        this();
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", timestamp=" + new Date(timestamp) +
                '}';
    }
}
