package sejong.foodsns.dto.board.update;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardUpdateTitleDto {

    private String orderTitle;
    private String updateTitle;
}
