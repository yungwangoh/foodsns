package sejong.foodsns.dto.member.blacklist;

import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberBlackListRequestDto {

    private Long id;
    private String reason;
    private ReportMember reportMember;

    public BlackList toEntity(MemberBlackListRequestDto memberBlackListRequestDto) {
        return BlackList.builder()
                .reason(getReason(memberBlackListRequestDto))
                .reportMember(getReportMember(memberBlackListRequestDto))
                .build();
    }

    private ReportMember getReportMember(MemberBlackListRequestDto memberBlackListRequestDto) {
        return memberBlackListRequestDto.getReportMember();
    }

    private String getReason(MemberBlackListRequestDto memberBlackListRequestDto) {
        return memberBlackListRequestDto.getReason();
    }
}
