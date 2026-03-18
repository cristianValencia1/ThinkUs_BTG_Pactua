package com.btg.funds.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscribeRequest {
    @NotBlank(message = "El id del cliente es obligatorio")
    private String clientId;

    @NotNull(message = "El id del fondo es obligatorio")
    private Long fundId;
}
