package sejong.foodsns.dto.member.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

@Schema(description = "회원 닉네임 수정")
@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class MemberUpdateUserNameDto {

    @Schema(description = "회원 이메일", example = "qwer1234@naver.com")
    private String email;

    @Schema(description = "회원 닉네임", example = "오광광박")
    private String username;
}
