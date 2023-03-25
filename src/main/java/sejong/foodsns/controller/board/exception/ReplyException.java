package sejong.foodsns.controller.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sejong.foodsns.log.error.ErrorResult;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice("sejong.foodsns.controller.board.reply")
public class ReplyException {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResult> notFoundException(IllegalArgumentException e) {
        log.info("[notFoundException]", e);

        return getErrorResultResponseEntity(NOT_FOUND, e);
    }

    private static ResponseEntity<ErrorResult> getErrorResultResponseEntity(HttpStatus status, Exception exception) {
        ErrorResult errorResult = new ErrorResult(status, exception.getMessage());
        return new ResponseEntity<>(errorResult, status);
    }
}
