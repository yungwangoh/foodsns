package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberReportService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberNumberOfCount.numOfReportFirst;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReportServiceImpl implements MemberReportService {

    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 신고 회원 저장
     * 유의 사항 -> (10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시.) 
     * @param memberRequestDto
     * @return 성공 : (신고회원 리스폰, Create), 실패 : (Exception)
     */
    @Override
    @Transactional
    public ResponseEntity<MemberReportResponseDto> reportMemberCreate(MemberRequestDto memberRequestDto) {

        Optional<Member> member = memberRepository.findById(memberRequestDto.getId());
        member.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ReportMember reportMember = new ReportMember(getMember(member));

        if(TheNumberOfReportIsTenOrMore(member)) {
            ReportMember save = reportMemberRepository.save(reportMember);
            return new ResponseEntity<>(new MemberReportResponseDto(save), CREATED);
        }
        // 10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시. 유의사항.
        else return new ResponseEntity<>(new MemberReportResponseDto(reportMember), ACCEPTED);
    }

    /**
     * 신고 회원 찾기
     * @param memberReportRequestDto
     * @return 성공 : (신고회원 리스폰, OK), 실패 : (Exception)
     */
    @Override
    public ResponseEntity<MemberReportResponseDto> reportMemberFindOne(MemberReportRequestDto memberReportRequestDto) {

        Optional<ReportMember> reportMember = reportMemberRepository.findById(reportMemberGetId(memberReportRequestDto));
        reportMember.orElseThrow(() -> new IllegalArgumentException("신고 회원이 존재하지 않습니다."));

        MemberReportResponseDto memberReportResponseDto = MemberReportResponseDto.builder()
                .reportMember(getReportMember(reportMember))
                .build();

        return new ResponseEntity<>(memberReportResponseDto, OK);
    }

    /**
     * 신고 회원 목록
     * @return 성공 : (신고회원 리스폰, OK)
     */
    @Override
    public ResponseEntity<List<MemberReportResponseDto>> reportMemberList() {
        List<ReportMember> reportMembers = reportMemberRepository.findAll();
        List<MemberReportResponseDto> reportResponseDtos = reportMembers.stream()
                .map(MemberReportResponseDto::new).collect(toList());

        return new ResponseEntity<>(reportResponseDtos, OK);
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
     * reportMember return
     * @param reportMember
     * @return reportMember
     */
    private ReportMember getReportMember(Optional<ReportMember> reportMember) {
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
