package com.btg.funds.infrastructure.notification;

import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;

public interface NotificationService {
    void sendSubscriptionNotification(ClientDocument client, TransactionDocument transaction);
}
