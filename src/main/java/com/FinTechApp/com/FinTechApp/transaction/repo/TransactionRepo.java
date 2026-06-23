package com.FinTechApp.com.FinTechApp.transaction.repo;
import com.FinTechApp.com.FinTechApp.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import java.util.List;
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccount_AccountNumber(String accountNumber, Pageable pageable);
    List<Transaction> findByAccount_AccountNumber(String accountNumber);
}
