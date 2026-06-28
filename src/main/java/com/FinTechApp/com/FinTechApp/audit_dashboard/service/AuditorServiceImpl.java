package com.FinTechApp.com.FinTechApp.audit_dashboard.service;
import com.FinTechApp.com.FinTechApp.account.dtos.AccountDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.FinTechApp.com.FinTechApp.account.repo.AccountRepo;
import com.FinTechApp.com.FinTechApp.auth_users.repo.UserRepo;
import com.FinTechApp.com.FinTechApp.transaction.repo.TransactionRepo;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditorServiceImpl implements AuditorService {

    private final UserRepo userRepo;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final ModelMapper modelMapper;

    @Override
    public Map<String, Long> getSystemTotals() {
        long totalsUsers = userRepo.count();
        long totalsAccounts = accountRepo.count();
        long totalsTransactions = transactionRepo.count();

        return Map.of(
            "users", totalsUsers,
            "accounts", totalsAccounts,
            "transactions", totalsTransactions
        );
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
                .map(account -> modelMapper.map(account, AccountDTO.class));
    }

    @Override
    public List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber) {
        return transactionRepo.findByAccount_AccountNumber(accountNumber)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TransactionDTO> findTransactionById(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class));
       
    }
    
    
}
