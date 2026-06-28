package com.FinTechApp.com.FinTechApp.transaction.services;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionDTO;
import com.FinTechApp.com.FinTechApp.transaction.dtos.TransactionRequest;
import com.FinTechApp.com.FinTechApp.res.Response;
import java.util.List;


public interface TransactionService {

    Response<?>createTransaction(TransactionRequest transactionRequest);

    Response<List<TransactionDTO>> getAllTransactionForMyAccount(String accountNumber, int page, int size);

    
}
