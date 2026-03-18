package com.btg.funds.infrastructure.persistence.mongo.document;

import com.btg.funds.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "funds")
public class FundDocument {
    @Id
    private Long id;
    private String code;
    private String name;
    private BigDecimal minimumAmount;
    private Category category;
    private boolean active;
}
