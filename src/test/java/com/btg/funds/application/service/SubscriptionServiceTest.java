package com.btg.funds.application.service;

import com.btg.funds.application.dto.SubscribeRequest;
import com.btg.funds.domain.enums.Category;
import com.btg.funds.domain.enums.NotificationPreference;
import com.btg.funds.domain.enums.SubscriptionStatus;
import com.btg.funds.infrastructure.exception.BusinessException;
import com.btg.funds.infrastructure.notification.NotificationService;
import com.btg.funds.infrastructure.persistence.mongo.document.ClientDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.FundDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.SubscriptionDocument;
import com.btg.funds.infrastructure.persistence.mongo.document.TransactionDocument;
import com.btg.funds.infrastructure.persistence.mongo.repository.SubscriptionMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private ClientService clientService;
    @Mock
    private FundService fundService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private SubscriptionMongoRepository subscriptionMongoRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private ClientDocument client;
    private FundDocument fund;

    @BeforeEach
    void setUp() {
        client = ClientDocument.builder()
                .id("client-1")
                .name("Christian")
                .email("christian@example.com")
                .phone("3001234567")
                .notificationPreference(NotificationPreference.EMAIL)
                .availableBalance(new BigDecimal("500000"))
                .build();

        fund = FundDocument.builder()
                .id(1L)
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .code("FPV_BTG_PACTUAL_RECAUDADORA")
                .minimumAmount(new BigDecimal("75000"))
                .category(Category.FPV)
                .active(true)
                .build();
    }

    @Test
    void shouldSubscribeSuccessfully() {
        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client-1");
        request.setFundId(1L);

        when(clientService.findById("client-1")).thenReturn(client);
        when(fundService.findById(1L)).thenReturn(fund);
        when(subscriptionMongoRepository.findByClientIdAndFundIdAndStatus("client-1", 1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());
        when(subscriptionMongoRepository.save(any(SubscriptionDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionService.save(any(TransactionDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(clientService.update(any(ClientDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(notificationService).sendSubscriptionNotification(any(ClientDocument.class), any(TransactionDocument.class));

        var response = subscriptionService.subscribe(request);

        assertEquals("SUCCESS", "SUCCESS");
        assertEquals(new BigDecimal("425000"), response.getClientBalance());
        assertEquals("ACTIVE", response.getSubscriptionStatus());
        verify(notificationService).sendSubscriptionNotification(any(ClientDocument.class), any(TransactionDocument.class));
    }

    @Test
    void shouldFailWhenBalanceIsInsufficient() {
        client.setAvailableBalance(new BigDecimal("10000"));

        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client-1");
        request.setFundId(1L);

        when(clientService.findById("client-1")).thenReturn(client);
        when(fundService.findById(1L)).thenReturn(fund);
        when(subscriptionMongoRepository.findByClientIdAndFundIdAndStatus("client-1", 1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> subscriptionService.subscribe(request));
        assertEquals("INSUFFICIENT_BALANCE", exception.getCode());
        assertEquals("No tiene saldo disponible para vincularse al fondo FPV_BTG_PACTUAL_RECAUDADORA", exception.getMessage());
    }
}
