package com.btg.funds.infrastructure.persistence.mongo.repository;

import com.btg.funds.infrastructure.persistence.mongo.document.FundDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundMongoRepository extends MongoRepository<FundDocument, Long> {
}
