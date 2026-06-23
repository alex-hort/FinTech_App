package com.FinTechApp.com.FinTechApp.transaction.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.FinTechApp.com.FinTechApp.enums.TransactionType;
import com.FinTechApp.com.FinTechApp.enums.TransactionStatus;
@Data @Builder @AllArgsConstructor @NoArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String description;
    private TransactionStatus status;
    private String sourceAccount;
    private String destinationAccount;
}
