package sejong.foodsns.dto.member;

import lombok.*;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;
import static sejong.foodsns.domain.member.MemberType.*;

@Data
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberRequestDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2 ~ 10자리여야 합니다.")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
            message = "비밀번호는 8 ~ 16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    public Member toEntity(MemberRequestDto memberRequestDto) {
        return Member.builder()
                .username(getUsername(memberRequestDto))
                .email(getEmail(memberRequestDto))
                .password(getPassword(memberRequestDto))
                .memberType(NORMAL)
                .build();
    }

    private String getPassword(MemberRequestDto memberRequestDto) {
        return memberRequestDto.getPassword();
    }

    private String getEmail(MemberRequestDto memberRequestDto) {
        return memberRequestDto.getEmail();
    }

    private String getUsername(MemberRequestDto memberRequestDto) {
        return memberRequestDto.getUsername();
    }
}
