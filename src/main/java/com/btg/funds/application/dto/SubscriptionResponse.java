package com.btg.funds.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class SubscriptionResponse {
    private String transactionId;
    private String subscriptionId;
    private String message;
    private BigDecimal clientBalance;
    private String subscriptionStatus;
}
