package com.denistiago.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class TransactionDTO {

    @NotNull
    @DecimalMin("0")
    private Double amount;

    @NotNull
    private Long timestamp;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
