package com.FinTechApp.com.FinTechApp.transaction.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.FinTechApp.com.FinTechApp.enums.TransactionType;
import java.math.BigDecimal;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String description;

    private String destinationAccountNumber; // for transfer transactions



  

}