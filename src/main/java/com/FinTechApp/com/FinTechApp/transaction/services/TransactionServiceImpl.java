package com.FinTechApp.com.FinTechApp.transaction.services;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionRequest;
import com.FinTechApp.com.FinTechApp.res.Response;
import java.util.List;
import com.FinTechApp.com.FinTechApp.enums.TransactionType;
import com.FinTechApp.com.FinTechApp.exceptions.BadRequestException;
import com.FinTechApp.com.FinTechApp.transaction.repo.TransactionRepo;
import com.FinTechApp.com.FinTechApp.account.repo.AccountRepo;
import com.FinTechApp.com.FinTechApp.notification.services.NotificationService;
import com.FinTechApp.com.FinTechApp.auth_users.services.UserService;
import com.FinTechApp.com.FinTechApp.enums.TransactionStatus;
import com.FinTechApp.com.FinTechApp.account.entity.Account;
import com.FinTechApp.com.FinTechApp.exceptions.NotFoundException;
import com.FinTechApp.com.FinTechApp.exceptions.InsufficientBalanceException;
import com.FinTechApp.com.FinTechApp.exceptions.InvalidTransactionException;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
import com.FinTechApp.com.FinTechApp.transaction.entity.Transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Account account;

    @Override
    @Transactional
    public Response<?> createTransaction(TransactionRequest transactionRequest) {
       Transaction transaction = new Transaction();

       transaction.setTransactionType(transactionRequest.getTransactionType());
       transaction.setAmount(transactionRequest.getAmount());
       transaction.setDescription(transactionRequest.getDescription());

       switch (transactionRequest.getTransactionType()) {
        case DEPOSIT -> handleDeposit(transactionRequest, transaction);
        case WITHDRAWAL -> handleWithdrawal(transactionRequest, transaction);
        case TRANSFER -> handleTransfer(transactionRequest, transaction);
        default -> throw new InvalidTransactionException("Invalid transaction type");
    
       }
       transaction.setStatus(TransactionStatus.SUCCESS);
       Transaction savTxn = transactionRepo.save(transaction);

       // Send notification out
       sendTransactionNotification(savTxn);

       return Response.builder()
               .statusCode(200)
               .message("Transaction successful")
               .data(savTxn)
               .build();




    }

    @Override
    @Transactional
    public Response<List<TransactionDTO>> getAllTransactionForMyAccount(String accountNumber, int page, int size) {
        User user = userService.getCurrentLoggedInUser();

        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    
        if(!account.getUser().equals(user.getId())){
            throw new BadRequestException("Account does not belong to the current user");
        }

        Pageable pegeable = PageRequest.of(page, size,  Sort.by("transactionDate").descending());
        Page<Transaction> txns = transactionRepo.findByAccount_AccountNumber(accountNumber, pegeable);

        List<TransactionDTO> txnDTOS = txns.getContent().stream()
        .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
        .toList();

        return Response.<List<TransactionDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transactions retrieved successfully")
                .data(txnDTOS)
                .meta(Map.of(
                    "currentPage", txns.getNumber(),
                    "totalItems", txns.getTotalElements(),
                    "totalPages", txns.getTotalPages(),
                    "pageSize", txns.getSize()
                ))
                .build();
        
    }


    private void handleDeposit(TransactionRequest transactionRequest, Transaction transaction) {
        Account account = accountRepo.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(transactionRequest.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);

    }

    private void handleWithdrawal(TransactionRequest transactionRequest, Transaction transaction) {
        Account account = accountRepo.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (account.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient funds for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);
    }

    private void handleTransfer(TransactionRequest transactionRequest, Transaction transaction) {
        Account sourceAccount = accountRepo.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Source account not found"));

        Account destinationAccount = accountRepo.findByAccountNumber(transactionRequest.getDestinationAccountNumber())
                .orElseThrow(() -> new NotFoundException("Destination account not found"));

        if (sourceAccount.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient funds for transfer");
        }

        //deduct from source and add to destination
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transactionRequest.getAmount()));
        accountRepo.save(sourceAccount);


         //add to destination account and deduct from source account
        destinationAccount.setBalance(destinationAccount.getBalance().add(transactionRequest.getAmount()));
        accountRepo.save(destinationAccount);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setDestinationAccount(destinationAccount.getAccountNumber());
    }


    private void sendTransactionNotification(Transaction tnx) {
        User user = tnx.getAccount().getUser();
        String subject;
        String template;

        Map<String, Object> templateModel = new HashMap<>();

        templateModel.put("name", user.getFirstName());
        templateModel.put("amount", tnx.getAmount());
        templateModel.put("accountNumber", tnx.getAccount().getAccountNumber());
        templateModel.put("date", tnx.getTransactionDate());
        templateModel.put("balance", tnx.getAccount().getBalance());

        if(tnx.getTransactionType() == TransactionType.DEPOSIT) {
            subject = "Credit Alert";
            template = "credit-alert";


            NotificationDTO notificationEmailSendOut = NotificationDTO.builder()
                    .recipientEmail(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateModel)
                    .build();

            notificationService.sendEmail(notificationEmailSendOut, user);


        } else if (tnx.getTransactionType() == TransactionType.WITHDRAWAL) {
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmailSendOut = NotificationDTO.builder()
                    .recipientEmail(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateModel)
                    .build();


            notificationService.sendEmail(notificationEmailSendOut, user);


            Account destination = accountRepo.findByAccountNumber(tnx.getDestinationAccount())
                    .orElseThrow(() -> new NotFoundException("Destination account not found"));

            User receiver = destination.getUser();

            Map<String, Object> recvVars = new HashMap<>();

            recvVars.put("name", receiver.getFirstName());
            recvVars.put("amount", tnx.getAmount());
            recvVars.put("accountNumber", destination.getAccountNumber());
            recvVars.put("date", tnx.getTransactionDate());
            recvVars.put("balance", tnx.getAccount().getBalance());

            NotificationDTO receiverNotification = NotificationDTO.builder()
                    .recipientEmail(receiver.getEmail())
                    .subject("Credit Alert")
                    .templateName("credit-alert")
                    .templateVariables(recvVars)
                    .build();

            notificationService.sendEmail(receiverNotification, receiver);


        } 


       
    }




    
}
