package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;

import java.util.List;

public interface MemberReportService {

    ResponseEntity<MemberReportResponseDto> reportMemberCreate(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberReportResponseDto> reportMemberFindOne(MemberReportRequestDto memberReportRequestDto);

    ResponseEntity<List<MemberReportResponseDto>> reportMemberList();
}
