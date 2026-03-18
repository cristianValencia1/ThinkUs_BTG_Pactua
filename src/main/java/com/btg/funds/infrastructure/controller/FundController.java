package com.btg.funds.infrastructure.controller;

import com.btg.funds.application.service.FundService;
import com.btg.funds.infrastructure.persistence.mongo.document.FundDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
public class FundController {
    private final FundService fundService;

    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    @GetMapping
    public List<FundDocument> findAll() {
        return fundService.findAll();
    }
}
