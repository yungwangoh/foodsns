package sejong.foodsns.controller.member.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RestControllerAdvice("sejong.foodsns.controller.member")
public class MemberException {

    /**
     * 중복된 회원 예외 처리
     * @param e
     * @return 잘못된 요청 400, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResult> duplicatedExceptionHandler(DuplicatedException e) {
        log.info("[duplicated exception]", e);
        return getErrorResultResponseEntity(BAD_REQUEST, e);
    }

    /**
     * 회원을 찾을 수 없는 예외처리
     * @param e
     * @return 서버에 요청한 것을 찾을 수 없음 404, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "요청한 것을 수행할 수 없음, 혹은 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResult> NoSearchMemberExceptionHandler(NoSearchMemberException e) {
        log.info("[no search member exception]", e);
        return getErrorResultResponseEntity(NOT_FOUND, e);
    }

    /**
     * 그 외의 요청한 것을 찾을 수 없을 때 예외처리
     * @param e
     * @return 요청한 것을 찾을 수 없음 404, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "요청한 것을 수행할 수 없음, 혹은 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResult> NotFoundException(IllegalArgumentException e) {
        log.info("[Not found Exception]", e);
        return getErrorResultResponseEntity(NOT_FOUND, e);
    }

    /**
     * IllegalStateException 에 대한 API status 예외 처리
     * @param e
     * @return 요청한 것을 수행할 수 없음 404, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "요청한 것을 수행할 수 없음, 혹은 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResult> NotFoundException(IllegalStateException e) {
        log.info("[Not Found Exception]", e);
        return getErrorResultResponseEntity(NOT_FOUND, e);
    }

    /**
     * JWT 토큰 만료에 대한 예외처리
     * @param e
     * @return 요청한 것을 수행할 수 없음 404, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "요청한 것을 수행할 수 없음, 혹은 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorResult> ExpiredJwtException(ExpiredJwtException e) {
        log.info("[ExpiredJwtException]", e);
        return getErrorResultResponseEntity(FORBIDDEN, e);
    }

    /**
     * 유효성 검사 오류
     * @param e
     * @return 잘못된 요청에 대한 상태 코드 401, Message
     */
    @ExceptionHandler
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청한 것을 수행할 수 없음, 혹은 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResult> ConstraintViolationException(ConstraintViolationException e) {
        log.info("[ConstrainViolationException]", e);
        return getErrorResultResponseEntity(BAD_REQUEST, e);
    }

    /**
     * Http 상태 코드와 Exception을 Json 형식으로 변환 후 반환하는 코드.
     * @param status
     * @param exception
     * @return ResponseEntity -> errorResult (Json 형식), status
     */
    private static ResponseEntity<ErrorResult> getErrorResultResponseEntity(HttpStatus status, Exception exception) {
        ErrorResult errorResult = new ErrorResult(status, exception.getMessage());
        return new ResponseEntity<>(errorResult, status);
    }
}
