package ai.boam.city.discovery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler to map exceptions to requested status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maps unexpected system failures to 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected system failure.");
    }

    /**
     * Maps missing parameters or type mismatches (e.g., non-double input) to 400 Bad Request.
     */
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<String> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest().body("Missing parameters or coordinate values out of range.");
    }
}
