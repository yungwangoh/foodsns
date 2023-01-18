package sejong.foodsns.dto.member.blacklist;

import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.ReportMember;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberBlackListRequestDto {

    private Long id;
    private String reason;
    private ReportMember reportMember;

    public BlackList toEntity() {
        return BlackList.builder()
                .reason(reason)
                .reportMember(reportMember)
                .build();
    }
}
