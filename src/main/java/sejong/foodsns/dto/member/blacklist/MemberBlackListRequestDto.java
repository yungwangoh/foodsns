package sejong.foodsns.dto.member.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberBlackListRequestDto {

    private Long id;
    private String reason;
    private Member member;

    public BlackList toEntity() {
        return BlackList.builder()
                .reason(reason)
                .member(member)
                .build();
    }
}
