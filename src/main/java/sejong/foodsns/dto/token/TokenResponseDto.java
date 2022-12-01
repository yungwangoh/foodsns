package sejong.foodsns.dto.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;
}
