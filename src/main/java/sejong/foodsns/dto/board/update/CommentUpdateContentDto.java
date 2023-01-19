package sejong.foodsns.dto.board.update;

import lombok.*;
import sejong.foodsns.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentUpdateContentDto {

    private String title;
    private String content;

//    @NotBlank
//    @Email
//    private String email;

    private String updateContent;
}
