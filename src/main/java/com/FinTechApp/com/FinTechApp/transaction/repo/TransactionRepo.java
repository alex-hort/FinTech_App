import com.FinTechApp.com.FinTechApp.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.FinTechApp.com.FinTechApp.account.entity.Account;


public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    
    Page<Transaction> findByAccount_AccountNumber(String accountNumber, Pageable pageable);
    List<Transaction> findByAccount_AccountNumber(String accountNumber);


}