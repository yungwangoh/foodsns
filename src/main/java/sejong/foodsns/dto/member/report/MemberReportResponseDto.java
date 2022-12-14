package sejong.foodsns.dto.member.report;

import lombok.*;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberReportResponseDto {

    private Long id;
    private Member member;

    @Builder
    public MemberReportResponseDto(ReportMember reportMember) {
        this.id = reportMember.getId();
        this.member = reportMember.getMember();
    }
}
