package com.btg.funds.application.service;

import com.btg.funds.domain.enums.Category;
import com.btg.funds.infrastructure.exception.BusinessException;
import com.btg.funds.infrastructure.persistence.mongo.document.FundDocument;
import com.btg.funds.infrastructure.persistence.mongo.repository.FundMongoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundService {
    private final FundMongoRepository fundMongoRepository;

    public FundService(FundMongoRepository fundMongoRepository) {
        this.fundMongoRepository = fundMongoRepository;
    }

    @PostConstruct
    public void seedFunds() {
        if (fundMongoRepository.count() > 0) {
            return;
        }

        fundMongoRepository.saveAll(List.of(
                FundDocument.builder().id(1L).code("FPV_BTG_PACTUAL_RECAUDADORA").name("FPV_BTG_PACTUAL_RECAUDADORA").minimumAmount(new BigDecimal("75000")).category(Category.FPV).active(true).build(),
                FundDocument.builder().id(2L).code("FPV_BTG_PACTUAL_ECOPETROL").name("FPV_BTG_PACTUAL_ECOPETROL").minimumAmount(new BigDecimal("125000")).category(Category.FPV).active(true).build(),
                FundDocument.builder().id(3L).code("DEUDAPRIVADA").name("DEUDAPRIVADA").minimumAmount(new BigDecimal("50000")).category(Category.FIC).active(true).build(),
                FundDocument.builder().id(4L).code("FDO-ACCIONES").name("FDO-ACCIONES").minimumAmount(new BigDecimal("250000")).category(Category.FIC).active(true).build(),
                FundDocument.builder().id(5L).code("FPV_BTG_PACTUAL_DINAMICA").name("FPV_BTG_PACTUAL_DINAMICA").minimumAmount(new BigDecimal("100000")).category(Category.FPV).active(true).build()
        ));
    }

    public List<FundDocument> findAll() {
        return fundMongoRepository.findAll();
    }

    public FundDocument findById(Long id) {
        return fundMongoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FUND_NOT_FOUND", "Fondo no encontrado o inactivo"));
    }
}
