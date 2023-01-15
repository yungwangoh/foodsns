package sejong.foodsns.dto.board.update;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class BoardUpdateTitleDto {

    @NotBlank
    private String orderTitle;

    @NotBlank
    @Pattern(regexp = "^{1,1000}$", message = "글자 제한 수는 50자 이내 입니다.")
    private String updateTitle;
}
