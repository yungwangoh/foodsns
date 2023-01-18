package sejong.foodsns.dto.member;

import lombok.*;
import sejong.foodsns.domain.member.Member;

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
    private String username;
    private String email;
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
