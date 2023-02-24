package sejong.foodsns.dto.member.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.dto.member.MemberResponseDto;

import static lombok.AccessLevel.*;

@Schema(description = "로그인 응답")
@Data
@NoArgsConstructor(access = PROTECTED)
@Builder
public class MemberLoginResponseDto {

    @Schema(description = "회원 응답")
    private MemberResponseDto memberResponseDto;
    @Schema(description = "JWT 토큰")
    private String token;

    public MemberLoginResponseDto(MemberResponseDto memberResponseDto, String token) {
        this.memberResponseDto = memberResponseDto;
        this.token = token;
    }
}
