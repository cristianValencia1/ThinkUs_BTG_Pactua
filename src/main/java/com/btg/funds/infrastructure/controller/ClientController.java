package com.btg.funds.infrastructure.controller;

import com.btg.funds.application.dto.BalanceResponse;
import com.btg.funds.application.dto.CreateClientRequest;
import com.btg.funds.application.service.ClientService;
import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ClientDocument create(@Valid @RequestBody CreateClientRequest request) {
        return clientService.create(request);
    }

    @GetMapping("/{clientId}")
    public ClientDocument findById(@PathVariable String clientId) {
        return clientService.findById(clientId);
    }

    @GetMapping("/{clientId}/balance")
    public BalanceResponse getBalance(@PathVariable String clientId) {
        return clientService.getBalance(clientId);
    }
}
