package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;
import sejong.foodsns.service.member.business.MemberBusinessService;

import java.util.Optional;

import static java.util.Optional.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.*;

@RequiredArgsConstructor
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
