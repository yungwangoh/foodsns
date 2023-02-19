package sejong.foodsns.dto.member.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

@Schema(description = "회원 비밀번호 수정")
@Data
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberUpdatePwdDto {

    @Schema(description = "회원 이메일", example = "qwer1234@naver.com")
    private String email;

    @Schema(description = "회원 비밀번호", example = "qwer1234@A")
    private String password;
}
