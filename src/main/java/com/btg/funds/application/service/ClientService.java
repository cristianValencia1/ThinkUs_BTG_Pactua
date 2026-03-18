package com.btg.funds.application.service;

import com.btg.funds.application.dto.BalanceResponse;
import com.btg.funds.application.dto.CreateClientRequest;
import com.btg.funds.infrastructure.exception.BusinessException;
import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import com.btg.funds.infrastructure.persistence.mongo.repository.ClientMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientMongoRepository clientMongoRepository;

    public ClientService(ClientMongoRepository clientMongoRepository) {
        this.clientMongoRepository = clientMongoRepository;
    }

    public ClientDocument create(CreateClientRequest request) {
        ClientDocument client = ClientDocument.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .notificationPreference(request.getNotificationPreference())
                .build();
        return clientMongoRepository.save(client);
    }

    public ClientDocument findById(String clientId) {
        return clientMongoRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("CLIENT_NOT_FOUND", "Cliente no encontrado"));
    }

    public BalanceResponse getBalance(String clientId) {
        ClientDocument client = findById(clientId);
        return BalanceResponse.builder()
                .clientId(client.getId())
                .availableBalance(client.getAvailableBalance())
                .build();
    }

    public ClientDocument update(ClientDocument client) {
        return clientMongoRepository.save(client);
    }
}
