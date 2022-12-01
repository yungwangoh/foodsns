package sejong.foodsns.exception.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ResponseStatus(HttpStatus.FORBIDDEN)
@RequiredArgsConstructor
public class ForbiddenException extends IllegalAccessException{
    public ForbiddenException(String s) {
        super(s);
    }
}
