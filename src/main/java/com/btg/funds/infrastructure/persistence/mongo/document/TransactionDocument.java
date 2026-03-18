package com.btg.funds.infrastructure.persistence.mongo.document;

import com.btg.funds.domain.enums.TransactionType;
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
@Document(collection = "transactions")
public class TransactionDocument {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String transactionId;
    private String clientId;
    private Long fundId;
    private String subscriptionId;
    private TransactionType type;
    private BigDecimal amount;
    private String status;
    private String message;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
