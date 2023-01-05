package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberBusinessService;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBusinessServiceImpl implements MemberBusinessService {

    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;

    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRankService(MemberRequestDto memberRequestDto) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));

        getMember(member).memberRecommendUp(getMember(member).getRecommendCount());

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberReportCount(MemberRequestDto memberRequestDto) {
        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));

        getMember(member).reportCount();

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberBlackListPenaltyCount(MemberRequestDto memberRequestDto) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));

        getMember(member).penaltyCount();

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRecommendCount(int recommendCount) {
        return null;
    }

    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    private MemberResponseDto getMemberResponseDto(Member save) {

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .member(save)
                .build();

        return memberResponseDto;
    }
}
