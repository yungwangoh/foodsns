package sejong.foodsns.log.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Schema(description = "error result")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResult {

    @Schema(description = "http error-code", defaultValue = "NOT_FOUND")
    private HttpStatus status;
    @Schema(description = "http error-message", defaultValue = "요청하신 기능을 수행할 수 없습니다.")
    private String message;
}
