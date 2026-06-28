package com.FinTechApp.com.FinTechApp.audit_dashboard.service;
import com.FinTechApp.com.FinTechApp.account.dtos.AccountDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String, Long> getSystemTotals();

    Optional<UserDTO> findUserByEmail(String email);

    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);

    Optional<TransactionDTO> findTransactionById(Long transactionId);

    
}
