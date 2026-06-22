import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;  



@ControllerAdvice
public class GlobalExceptionHandler {
   
   @ExceptionHandler(Exception.class)
   public ResponseEntity<Response> handleAllUnkownExceptions(Exception ex) {
    Response<?> response = Responsee.builder()
    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    .message(ex.getMessage())
    .build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
   }



   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
    Response<?> response = Responsee.builder()
    .statusCode(HttpStatus.NOT_FOUND.value())
    .message(ex.getMessage())
    .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
   }


   @ExceptionHandler(InsufficientFundsException.class)
   public ResponseEntity<Response> handleInsufficientFundsException(InsufficientFundsException ex) {
    Response<?> response = Responsee.builder()
    .statusCode(HttpStatus.BAD_REQUEST.value())
    .message(ex.getMessage())
    .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
   }


   @ExceptionHandler(InvalidTransactionException.class)
   public ResponseEntity<Response> handleInvalidTransactionException(InvalidTransactionException ex) {
    Response<?> response = Responsee.builder()
    .statusCode(HttpStatus.BAD_REQUEST.value())
    .message(ex.getMessage())
    .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
   }


   @ExceptionHandler(BadRequestException.class)
   public ResponseEntity<Response> handleBadRequestException(BadRequestException ex) {
    Response<?> response = Responsee.builder()
    .statusCode(HttpStatus.BAD_REQUEST.value())
    .message(ex.getMessage())
    .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
   }



}