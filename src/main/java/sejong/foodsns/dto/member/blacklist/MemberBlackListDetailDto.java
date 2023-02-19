package sejong.foodsns.dto.member.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.dto.member.MemberResponseDto;

@Schema(description = "블랙리스트 회원 세부 정보")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberBlackListDetailDto {

    @Schema(description = "블랙리스트 id", defaultValue = "0")
    private Long id;
    @Schema(description = "블랙리스트 된 사유", example = "신고 수가 10개 이상.")
    private String reason;
    @Schema(description = "회원 반환 정보")
    private MemberResponseDto memberResponseDto;

    public MemberBlackListDetailDto(BlackList blackList) {
        this.reason = blackList.getReason();
        this.memberResponseDto = new MemberResponseDto(blackList.getMember());
    }
}
