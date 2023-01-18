package sejong.foodsns.dto.member.report;

import lombok.*;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberResponseDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberReportResponseDto {

    private Long id;
    private MemberResponseDto memberResponseDto;

    public MemberReportResponseDto(ReportMember reportMember) {
        this.id = reportMember.getId();
        this.memberResponseDto = MemberResponseDto.builder()
                .member(reportMember.getMember())
                .build();
    }
}
