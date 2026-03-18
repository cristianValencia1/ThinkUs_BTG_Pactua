package com.btg.funds.infrastructure.persistence.mongo.repository;

import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {
    List<TransactionDocument> findByClientIdOrderByCreatedAtDesc(String clientId);
}
