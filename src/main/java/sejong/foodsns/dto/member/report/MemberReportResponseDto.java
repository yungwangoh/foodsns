package sejong.foodsns.dto.member.report;

import lombok.*;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberReportResponseDto {

    private Member member;

    public MemberReportResponseDto(ReportMember reportMember) {
        this.member = reportMember.getMember();
    }
}
