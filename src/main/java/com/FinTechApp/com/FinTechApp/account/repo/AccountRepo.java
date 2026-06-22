
import com.FinTechApp.com.FinTechApp.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(Long userId);
}
