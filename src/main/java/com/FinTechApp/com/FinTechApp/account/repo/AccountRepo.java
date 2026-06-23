package com.FinTechApp.com.FinTechApp.account.repo;
import com.FinTechApp.com.FinTechApp.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(Long userId);
}
