package sejong.foodsns.dto.member.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PROTECTED;

@Schema(description = "회원 로그인")
@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class MemberLoginDto {

    @Schema(description = "회원 이메일", example = "qwer1234@naver.com")
    private String email;

    @Schema(description = "회원 비밀번호", example = "qwer1234@A")
    private String password;
}
