
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.FinTechApp.com.FinTechApp.enums.AccountType;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import com.FinTechApp.com.FinTechApp.enums.Currency;
import com.FinTechApp.com.FinTechApp.enums.AccountStatus;
import java.util.List;
import java.util.ArrayList;
import com.FinTechApp.com.FinTechApp.transaction.entity.Transaction;


@Entity
@Data
@Builder
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String accountNumber;

    @Column(nullable= false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt = LocalDateTime.now();




}