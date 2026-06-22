import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.enums.AccountStatus;
import com.FinTechApp.com.FinTechApp.enums.AccountType;
import com.FinTechApp.com.FinTechApp.enums.Currency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;


    @JsonBackReference
    private UserDTO user;

    private Currency currency;

    private AccountStatus status;

    @JsonManagedReference
    private List<TransactionDTO> transactions;

    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
   

  


}