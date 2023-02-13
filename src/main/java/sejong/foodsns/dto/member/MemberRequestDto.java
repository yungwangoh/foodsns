package sejong.foodsns.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;
import static sejong.foodsns.domain.member.MemberType.*;

@Schema(description = "회원 요청")
@Data
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberRequestDto {

    private Long id;
    @Schema(description = "회원 닉네임", example = "오광광박")
    private String username;
    @Schema(description = "회원 이메일", example = "qwer1234@naver.com")
    private String email;
    @Schema(description = "회원 비밀번호 (특수문자, 대문자 하나 이상 무조건 포함.)", example = "qwer1234@A")
    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .memberType(NORMAL)
                .build();
    }
}
