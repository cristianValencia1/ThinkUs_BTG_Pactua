package com.btg.funds.application.service;

import com.btg.funds.application.dto.SubscribeRequest;
import com.btg.funds.application.dto.SubscriptionResponse;
import com.btg.funds.domain.enums.SubscriptionStatus;
import com.btg.funds.domain.enums.TransactionType;
import com.btg.funds.infrastructure.exception.BusinessException;
import com.btg.funds.infrastructure.notification.NotificationService;
import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.FundDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.SubscriptionDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import com.btg.funds.infrastructure.persistence.mongo.repository.SubscriptionMongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final ClientService clientService;
    private final FundService fundService;
    private final TransactionService transactionService;
    private final SubscriptionMongoRepository subscriptionMongoRepository;
    private final NotificationService notificationService;

    public SubscriptionService(ClientService clientService,
                               FundService fundService,
                               TransactionService transactionService,
                               SubscriptionMongoRepository subscriptionMongoRepository,
                               NotificationService notificationService) {
        this.clientService = clientService;
        this.fundService = fundService;
        this.transactionService = transactionService;
        this.subscriptionMongoRepository = subscriptionMongoRepository;
        this.notificationService = notificationService;
    }

    public SubscriptionResponse subscribe(SubscribeRequest request) {
        ClientDocument client = clientService.findById(request.getClientId());
        FundDocument fund = fundService.findById(request.getFundId());

        if (!fund.isActive()) {
            throw new BusinessException("FUND_NOT_FOUND", "Fondo no encontrado o inactivo");
        }

        subscriptionMongoRepository.findByClientIdAndFundIdAndStatus(client.getId(), fund.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(existing -> {
                    throw new BusinessException("SUBSCRIPTION_ALREADY_EXISTS", "El cliente ya tiene una suscripción activa a este fondo");
                });

        if (client.getAvailableBalance().compareTo(fund.getMinimumAmount()) < 0) {
            throw new BusinessException("INSUFFICIENT_BALANCE",
                    "No tiene saldo disponible para vincularse al fondo " + fund.getName());
        }

        client.setAvailableBalance(client.getAvailableBalance().subtract(fund.getMinimumAmount()));
        clientService.update(client);

        SubscriptionDocument subscription = subscriptionMongoRepository.save(SubscriptionDocument.builder()
                .clientId(client.getId())
                .fundId(fund.getId())
                .amount(fund.getMinimumAmount())
                .status(SubscriptionStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .build());

        TransactionDocument transaction = transactionService.save(TransactionDocument.builder()
                .transactionId(generateTransactionId())
                .clientId(client.getId())
                .fundId(fund.getId())
                .subscriptionId(subscription.getId())
                .type(TransactionType.OPENING)
                .amount(fund.getMinimumAmount())
                .status("SUCCESS")
                .message("Suscripción realizada correctamente")
                .build());

        notificationService.sendSubscriptionNotification(client, transaction);

        return SubscriptionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .subscriptionId(subscription.getId())
                .message(transaction.getMessage())
                .clientBalance(client.getAvailableBalance())
                .subscriptionStatus(subscription.getStatus().name())
                .build();
    }

    public SubscriptionResponse cancel(String subscriptionId) {
        SubscriptionDocument subscription = subscriptionMongoRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException("SUBSCRIPTION_NOT_FOUND", "Suscripción activa no encontrada"));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessException("SUBSCRIPTION_NOT_FOUND", "Suscripción activa no encontrada");
        }

        ClientDocument client = clientService.findById(subscription.getClientId());
        client.setAvailableBalance(client.getAvailableBalance().add(subscription.getAmount()));
        clientService.update(client);

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(LocalDateTime.now());
        subscriptionMongoRepository.save(subscription);

        TransactionDocument transaction = transactionService.save(TransactionDocument.builder()
                .transactionId(generateTransactionId())
                .clientId(client.getId())
                .fundId(subscription.getFundId())
                .subscriptionId(subscription.getId())
                .type(TransactionType.CANCELLATION)
                .amount(subscription.getAmount())
                .status("SUCCESS")
                .message("Cancelación realizada correctamente")
                .build());

        return SubscriptionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .subscriptionId(subscription.getId())
                .message(transaction.getMessage())
                .clientBalance(client.getAvailableBalance())
                .subscriptionStatus(subscription.getStatus().name())
                .build();
    }

    public List<SubscriptionDocument> findByClientId(String clientId) {
        clientService.findById(clientId);
        return subscriptionMongoRepository.findByClientIdOrderByOpenedAtDesc(clientId);
    }

    private String generateTransactionId() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
