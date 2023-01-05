package sejong.foodsns.dto.member.report;

import lombok.*;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberReportRequestDto {

    private Long id;
    private Member member;

    public ReportMember toEntity() {
        return ReportMember.builder()
                .member(member)
                .build();
    }
}
