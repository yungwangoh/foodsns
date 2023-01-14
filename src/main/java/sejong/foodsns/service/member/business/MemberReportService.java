package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberReportService {

    ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate(String email);

    ResponseEntity<Optional<MemberReportResponseDto>> reportMemberFindOne(Long id);

    ResponseEntity<Optional<List<MemberReportResponseDto>>> reportMemberList();
}
