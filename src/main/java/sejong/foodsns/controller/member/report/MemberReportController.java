package sejong.foodsns.controller.member.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;
import sejong.foodsns.service.member.business.MemberReportService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class MemberReportController {

    private final MemberReportService memberReportService;

    /**
     * 신고 회원 생성 컨트롤러
     * @param memberReportRequestDto 신고회원 Dto
     * @return 성공 : 신고회원, CREATE | 실패 : 신고회원, NOT_FOUND
     *  | 유의 사항 : 신고 수가 10개 미만이면 ACCEPT
     */
    @PostMapping("/member/report")
    ResponseEntity<MemberReportResponseDto> reportMemberCreate(@RequestBody @Valid MemberReportRequestDto memberReportRequestDto) {

        ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate =
                memberReportService.reportMemberCreate(memberReportRequestDto);

        return new ResponseEntity<>(getBody(reportMemberCreate).get(), reportMemberCreate.getStatusCode());
    }

    /**
     * 신고 회원 단건 조회
     * @param id 신고 회원 id(pk)
     * @return 성고 : 신고 회원 Dto, OK | 실패 : 신고 회원 Dto, NOT_FOUND
     */
    @GetMapping("/member/report/{id}")
    ResponseEntity<MemberReportResponseDto> reportMemberSearch(@PathVariable("id") Long id) {

        ResponseEntity<Optional<MemberReportResponseDto>> reportMemberFindOne = memberReportService.reportMemberFindOne(id);

        return new ResponseEntity<>(getBody(reportMemberFindOne).get(), reportMemberFindOne.getStatusCode());
    }

    /**
     * 신고 회원 목록
     * @return 성공 : 신고 회원 목록, OK | 실패는 존재하지 않음.
     */
    @GetMapping("/member/reports")
    ResponseEntity<List<MemberReportResponseDto>> reportMemberList() {

        ResponseEntity<Optional<List<MemberReportResponseDto>>> reportMemberList = memberReportService.reportMemberList();

        return new ResponseEntity<>(getReportMemberListBody(reportMemberList).get(), reportMemberList.getStatusCode());
    }

    private Optional<List<MemberReportResponseDto>> getReportMemberListBody(ResponseEntity<Optional<List<MemberReportResponseDto>>> reportMemberList) {
        return reportMemberList.getBody();
    }

    private Optional<MemberReportResponseDto> getBody(ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate) {
        return reportMemberCreate.getBody();
    }
}
