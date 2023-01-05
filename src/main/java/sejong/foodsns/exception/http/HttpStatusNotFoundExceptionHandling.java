package sejong.foodsns.exception.http;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@Component
@ResponseStatus(value = NOT_FOUND)
public class HttpStatusNotFoundExceptionHandling extends IllegalArgumentException{

    public HttpStatusNotFoundExceptionHandling() {
        super();
    }
}
