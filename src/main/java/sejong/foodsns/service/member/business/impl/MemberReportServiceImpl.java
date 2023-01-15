package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;
import sejong.foodsns.exception.http.member.NoSearchMemberException;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberReportService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberNumberOfCount.numOfReportFirst;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberReportServiceImpl implements MemberReportService {

    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 신고 회원 저장
     * 유의 사항 -> (10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시.)
     * @param email 본인 이메일
     * @return 성공 : (신고회원 리스폰, Create), 실패 : (Exception)
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate(String email) {

        Optional<Member> member = of(memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new NoSearchMemberException("회원이 존재하지 않습니다.")));

        ReportMember reportMember = new ReportMember(getMember(member));

        if(TheNumberOfReportIsTenOrMore(member)) {
            ReportMember save = reportMemberRepository.save(reportMember);
            return new ResponseEntity<>(of(new MemberReportResponseDto(save)), CREATED);
        }
        // 10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시. 유의사항.
        else return new ResponseEntity<>(of(new MemberReportResponseDto(reportMember)), ACCEPTED);
    }

    /**
     * 신고 회원 찾기
     *
     * @param id
     * @return 성공 : (신고회원 리스폰, OK), 실패 : (Exception)
     */
    @Override
    public ResponseEntity<Optional<MemberReportResponseDto>> reportMemberFindOne(Long id) {

        Optional<ReportMember> reportMember = of(reportMemberRepository.findById(id)
                .orElseThrow(() -> new NoSearchMemberException("신고 회원이 존재하지 않습니다.")));

        MemberReportResponseDto memberReportResponseDto = new MemberReportResponseDto(getReportMember(reportMember));

        return new ResponseEntity<>(of(memberReportResponseDto), OK);
    }

    /**
     * 신고 회원 목록
     *
     * @return 성공 : (신고회원 리스폰, OK)
     */
    @Override
    public ResponseEntity<Optional<List<MemberReportResponseDto>>> reportMemberList() {
        List<ReportMember> reportMembers = reportMemberRepository.findAll();
        List<MemberReportResponseDto> reportResponseDtos = reportMembers.stream()
                .map(MemberReportResponseDto::new).collect(toList());

        return new ResponseEntity<>(of(reportResponseDtos), OK);
    }

    /**
     * 신고 개수가 10개 이상
     * @param member
     * @return success : true, fail : false
     */
    private boolean TheNumberOfReportIsTenOrMore(Optional<Member> member) {
        return getMember(member).getReportCount() >= numOfReportFirst;
    }

    /**
     * Optional member return member
     * @param member
     * @return member
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    /**
     * Member return
     * @param memberReportRequestDto
     * @return member
     */
    private Member getMember(MemberReportRequestDto memberReportRequestDto) {
        return memberReportRequestDto.getMember();
    }

    /**
     * reportMember return
     * @param reportMember
     * @return reportMember
     */
    private static ReportMember getReportMember(Optional<ReportMember> reportMember) {
        return reportMember.get();
    }

    /**
     * reportMember return id
     * @param memberReportRequestDto
     * @return reportMember id
     */
    private Long reportMemberGetId(MemberReportRequestDto memberReportRequestDto) {
        return memberReportRequestDto.getId();
    }
}
