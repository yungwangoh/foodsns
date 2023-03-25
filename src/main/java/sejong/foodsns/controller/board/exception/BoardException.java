package sejong.foodsns.controller.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sejong.foodsns.exception.http.DuplicatedException;
import sejong.foodsns.exception.http.member.NoSearchMemberException;
import sejong.foodsns.log.error.ErrorResult;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice("sejong.foodsns.controller.board")
public class BoardException {

    /**
     * 중복된 제목 예외 처리
     * @param e
     * @return 잘못된 요청 400, Message
     */
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResult> duplicatedExceptionHandler(DuplicatedException e) {
        log.info("[duplicated exception]", e);
        return getErrorResultResponseEntity(BAD_REQUEST, e);
    }

    /**
     * 제목 글자수 제한
     * @param e
     * @return 요청된 데이터가 수정되지 않음 304, Message
     */
    @ExceptionHandler
    @ResponseStatus(NOT_MODIFIED)
    public ResponseEntity<ErrorResult> handlingLimitedOfCharactersError(ConstraintViolationException e) {
        log.info("[handling Limited Of Characters Error]", e);
        return getErrorResultResponseEntity(NOT_MODIFIED, e);
    }
    private static ResponseEntity<ErrorResult> getErrorResultResponseEntity(HttpStatus status, Exception exception) {
        ErrorResult errorResult = new ErrorResult(status, exception.getMessage());
        return new ResponseEntity<>(errorResult, status);
    }
}
