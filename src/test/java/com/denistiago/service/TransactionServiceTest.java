package com.denistiago.service;


import com.denistiago.domain.Transaction;
import com.denistiago.events.TransactionCreatedEvent;
import com.denistiago.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService transactionService;

    private TransactionRepository repositoryMock;
    private ApplicationEventPublisher eventPublisherMock;

    @Before
    public void before() {
        this.repositoryMock = mock(TransactionRepository.class);
        this.eventPublisherMock = mock(ApplicationEventPublisher.class);
        this.transactionService = new TransactionService(eventPublisherMock, repositoryMock);
    }

    @Test
    public void shouldExpectTransactionCreatedEventOnTransactionCreation() {
        transactionService.create(new Transaction());
        verify(repositoryMock, times(1)).create(any(Transaction.class));
        verify(eventPublisherMock, times(1)).publishEvent(any(TransactionCreatedEvent.class));
    }

}
