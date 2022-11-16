package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberNumberOfCount;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberReportService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberNumberOfCount.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReportServiceImpl implements MemberReportService {

    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 신고 회원 저장 -> 성공 201 
     * 유의 사항 -> (10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시.) 
     * @param memberRequestDto
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<MemberReportResponseDto> reportMemberCreate(MemberRequestDto memberRequestDto) {

        Optional<Member> member = memberRepository.findById(memberRequestDto.getId());
        ReportMember reportMember = new ReportMember(getMember(member));

        if(TheNumberOfReportIsTenOrMore(member)) {
            ReportMember save = reportMemberRepository.save(reportMember);
            return new ResponseEntity<>(new MemberReportResponseDto(save), CREATED);
        }
        // 10개 미만은 에러가 아님 -> 203으로 처리되지 않음을 표시. 유의사항.
        else return new ResponseEntity<>(new MemberReportResponseDto(reportMember), ACCEPTED);
    }

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
     * @return
     */
    private boolean TheNumberOfReportIsTenOrMore(Optional<Member> member) {
        return getMember(member).getReportCount() >= numOfReportFirst;
    }

    /**
     * Optional member return member
     * @param member
     * @return
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }
}
