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

    public ReportMember toEntity(MemberReportRequestDto memberReportRequestDto) {
        return ReportMember.builder()
                .member(getMember(memberReportRequestDto))
                .build();
    }

    private Member getMember(MemberReportRequestDto memberReportRequestDto) {
        return memberReportRequestDto.getMember();
    }
}
