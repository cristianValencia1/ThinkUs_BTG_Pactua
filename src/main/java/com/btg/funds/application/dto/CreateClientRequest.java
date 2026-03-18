package com.btg.funds.application.dto;

import com.btg.funds.domain.enums.NotificationPreference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateClientRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "El correo no es válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotNull(message = "La preferencia de notificación es obligatoria")
    private NotificationPreference notificationPreference;
}
