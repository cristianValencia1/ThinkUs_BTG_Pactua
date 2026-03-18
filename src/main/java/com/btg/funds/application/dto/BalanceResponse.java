package com.btg.funds.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BalanceResponse {
    private String clientId;
    private BigDecimal availableBalance;
}
