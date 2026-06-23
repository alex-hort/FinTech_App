package com.FinTechApp.com.FinTechApp.account.dtos;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.enums.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    @JsonBackReference private UserDTO user;
    private Currency currency;
    private AccountStatus status;
    @JsonManagedReference private List<TransactionDTO> transactions;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
