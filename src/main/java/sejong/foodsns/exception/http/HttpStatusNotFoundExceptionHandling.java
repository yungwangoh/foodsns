package sejong.foodsns.exception.http;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@Component
@ResponseStatus(value = NOT_FOUND)
@RequiredArgsConstructor
public class HttpStatusNotFoundExceptionHandling extends IllegalArgumentException{

    public HttpStatusNotFoundExceptionHandling(String s) {
        super(s);
    }
}
