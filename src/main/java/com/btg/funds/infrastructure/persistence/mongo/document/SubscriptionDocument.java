package com.btg.funds.infrastructure.persistence.mongo.document;

import com.btg.funds.domain.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
public class SubscriptionDocument {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String clientId;
    private Long fundId;
    private BigDecimal amount;

    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Builder.Default
    private LocalDateTime openedAt = LocalDateTime.now();

    private LocalDateTime cancelledAt;
}
