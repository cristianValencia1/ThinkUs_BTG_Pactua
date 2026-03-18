package com.btg.funds.infrastructure.controller;

import com.btg.funds.application.service.TransactionService;
import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionDocument> findByClientId(@PathVariable String clientId) {
        return transactionService.findByClientId(clientId);
    }
}
