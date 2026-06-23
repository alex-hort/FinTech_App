package com.FinTechApp.com.FinTechApp.exceptions;
import com.FinTechApp.com.FinTechApp.res.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Response<?>> handleFunds(InsufficientFundsException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Response<?>> handleInvalidTx(InvalidTransactionException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<?>> handleBadRequest(BadRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    private ResponseEntity<Response<?>> build(HttpStatus status, String message) {
        return new ResponseEntity<>(Response.builder().statusCode(status.value()).message(message).build(), status);
    }
}
