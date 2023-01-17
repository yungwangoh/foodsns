package sejong.foodsns.dto.member.blacklist;

import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberBlackListCreateRequestDto {

    private Long id;
    private String reason;
    private MemberRequestDto memberRequestDto;

    public BlackList toEntity() {
        return BlackList.builder()
                .reason(reason)
                .member(memberRequestDto.toEntity())
                .build();
    }
}
