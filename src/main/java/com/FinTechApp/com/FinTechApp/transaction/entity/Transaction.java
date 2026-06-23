package com.FinTechApp.com.FinTechApp.transaction.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.FinTechApp.com.FinTechApp.enums.TransactionType;
import com.FinTechApp.com.FinTechApp.enums.TransactionStatus;
import com.FinTechApp.com.FinTechApp.account.entity.Account;
@Entity @Data @Builder @Table(name = "transactions") @AllArgsConstructor @NoArgsConstructor
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private TransactionType transactionType;
    @Column(nullable = false) private LocalDateTime transactionDate = LocalDateTime.now();
    private String description;
    @Enumerated(EnumType.STRING) private TransactionStatus status;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "account_id", nullable = false) private Account account;
    private String sourceAccount;
    private String destinationAccount;
}
