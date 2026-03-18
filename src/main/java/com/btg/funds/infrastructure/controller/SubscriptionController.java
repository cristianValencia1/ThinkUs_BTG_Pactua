package com.btg.funds.infrastructure.controller;

import com.btg.funds.application.dto.SubscribeRequest;
import com.btg.funds.application.dto.SubscriptionResponse;
import com.btg.funds.application.service.SubscriptionService;
import com.btg.funds.infrastructure.persistence.mongo.document.SubscriptionDocument;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscriptions")
    public SubscriptionResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return subscriptionService.subscribe(request);
    }

    @PostMapping("/subscriptions/{subscriptionId}/cancel")
    public SubscriptionResponse cancel(@PathVariable String subscriptionId) {
        return subscriptionService.cancel(subscriptionId);
    }

    @GetMapping("/clients/{clientId}/subscriptions")
    public List<SubscriptionDocument> findByClient(@PathVariable String clientId) {
        return subscriptionService.findByClientId(clientId);
    }
}
