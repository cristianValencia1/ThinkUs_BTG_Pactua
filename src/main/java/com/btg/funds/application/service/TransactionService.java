package com.btg.funds.application.service;

import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import com.btg.funds.infrastructure.persistence.mongo.repository.TransactionMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionMongoRepository transactionMongoRepository;

    public TransactionService(TransactionMongoRepository transactionMongoRepository) {
        this.transactionMongoRepository = transactionMongoRepository;
    }

    public TransactionDocument save(TransactionDocument transaction) {
        return transactionMongoRepository.save(transaction);
    }

    public List<TransactionDocument> findByClientId(String clientId) {
        return transactionMongoRepository.findByClientIdOrderByCreatedAtDesc(clientId);
    }
}
