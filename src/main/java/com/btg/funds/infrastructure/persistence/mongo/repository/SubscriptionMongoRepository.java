package com.btg.funds.infrastructure.persistence.mongo.repository;

import com.btg.funds.domain.enums.SubscriptionStatus;
import com.btg.funds.infrastructure.persistence.mongo.document.SubscriptionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionMongoRepository extends MongoRepository<SubscriptionDocument, String> {
    Optional<SubscriptionDocument> findByClientIdAndFundIdAndStatus(String clientId, Long fundId, SubscriptionStatus status);
    List<SubscriptionDocument> findByClientIdOrderByOpenedAtDesc(String clientId);
}
