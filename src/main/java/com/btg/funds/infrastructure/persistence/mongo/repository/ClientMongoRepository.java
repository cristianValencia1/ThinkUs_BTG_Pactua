package com.btg.funds.infrastructure.persistence.mongo.repository;

import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientMongoRepository extends MongoRepository<ClientDocument, String> {
}
