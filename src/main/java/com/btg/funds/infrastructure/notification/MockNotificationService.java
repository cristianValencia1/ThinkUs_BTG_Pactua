package com.btg.funds.infrastructure.notification;

import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockNotificationService implements NotificationService {
    @Override
    public void sendSubscriptionNotification(ClientDocument client, TransactionDocument transaction) {
        log.info("Notificación {} enviada a {} para la transacción {}",
                client.getNotificationPreference(),
                client.getNotificationPreference().name().equals("EMAIL") ? client.getEmail() : client.getPhone(),
                transaction.getTransactionId());
    }
}
