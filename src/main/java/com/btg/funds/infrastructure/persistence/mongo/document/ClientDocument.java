package com.btg.funds.infrastructure.persistence.mongo.document;

import com.btg.funds.domain.enums.NotificationPreference;
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
@Document(collection = "clients")
public class ClientDocument {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;
    private String email;
    private String phone;
    private NotificationPreference notificationPreference;

    @Builder.Default
    private BigDecimal availableBalance = new BigDecimal("500000");

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
