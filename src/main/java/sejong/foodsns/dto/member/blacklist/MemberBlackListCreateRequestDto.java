package sejong.foodsns.dto.member.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;

@Schema(description = "블랙리스트 회원 추가")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberBlackListCreateRequestDto {

    @Schema(description = "블랙리스트 id", defaultValue = "0")
    private Long id;
    @Schema(description = "블랙리스트 된 사유", example = "신고 수가 10개 이상.")
    private String reason;
    @Schema(description = "회원 요청 DTO")
    private MemberRequestDto memberRequestDto;

    public BlackList toEntity() {
        return BlackList.builder()
                .reason(reason)
                .member(memberRequestDto.toEntity())
                .build();
    }
}
