package sejong.foodsns.dto.member.blacklist;

import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.dto.member.MemberResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberBlackListDetailDto {

    private Long id;
    private String reason;
    private MemberResponseDto memberResponseDto;

    public MemberBlackListDetailDto(BlackList blackList) {
        this.reason = blackList.getReason();
        this.memberResponseDto = new MemberResponseDto(blackList.getMember());
    }
}
