package co.com.jorge.cashout.controller.handler;

import co.com.jorge.cashout.exceptions.Error400Exception;
import co.com.jorge.cashout.exceptions.NotFoundCashOutException;
import co.com.jorge.cashout.exceptions.NotFoundUserException;
import co.com.jorge.cashout.exceptions.responses.ErrorResponse;
import co.com.jorge.cashout.exceptions.responses.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

@ControllerAdvice
public class GlobalHandlerError {

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundUserException(NotFoundUserException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse(
          HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage())));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException e) {
        List<ValidationErrorResponse.Violation> errors = e.getBindingResult().getFieldErrors().stream()
          .map(error -> new ValidationErrorResponse.Violation(error.getField(), error.getDefaultMessage()))
          .toList();
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errors);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name(),
            "Validation error",
            errorResponse.getErrors())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
          HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage())));
    }

    @ExceptionHandler(NotFoundCashOutException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundCashOutException(NotFoundCashOutException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse(
          HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage())));
    }

    @ExceptionHandler(Error400Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> handleError400Exception(Error400Exception e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
          HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage())));
    }
}
