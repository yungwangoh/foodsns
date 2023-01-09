package sejong.foodsns.exception.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 공통 예외 (게시물, 회원)
 */
@Component
@ResponseStatus(HttpStatus.FORBIDDEN)
@RequiredArgsConstructor
public class ForbiddenException extends IllegalAccessException{
    public ForbiddenException(String s) {
        super(s);
    }
}
