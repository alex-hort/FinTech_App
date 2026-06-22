
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String error) {
        super(error);
    }
}