package sejong.foodsns.dto.board.update;

import lombok.Getter;
import sejong.foodsns.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CommentUpdateContentDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

//    @NotBlank
//    @Email
//    private String email;

    @NotBlank
    @Pattern(regexp = "^{1,1000}$", message = "글자 제한 수는 50자 이내 입니다.")
    private String updateContent;
}
