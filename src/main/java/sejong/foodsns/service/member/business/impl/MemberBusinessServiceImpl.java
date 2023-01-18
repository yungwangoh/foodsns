package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBusinessService;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.domain.member.MemberType.BLACKLIST;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBusinessServiceImpl implements MemberBusinessService {
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;

    /**
     * 회원 등급 서비스
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRankService(String email) {

        Optional<Member> member = getMember(email);

        getMember(member).memberRankUp(getMember(member).getRecommendCount());

        MemberResponseDto memberResponseDto = getMemberResponseDto(getMember(member));

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 신고 회원 카운트
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberReportCount(String email) {
        Optional<Member> member = getMember(email);

        getMember(member).reportCount();

        MemberResponseDto memberResponseDto = getMemberResponseDto(getMember(member));

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 회원 추천 수 업데이트 (게시물에 받은 추천수를 맴버로 업데이트 {Mapping} 초기 구현)
     *
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRecommendUp(String email) {

        Optional<Member> member = getMember(email);

        getMember(member).memberRecommendCount();

        MemberResponseDto memberResponseDto = getMemberResponseDto(getMember(member));

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 회원 타입 블랙리스트로 변경
     *
     * @param email@return 회원 응답 Dto, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberBlackListTypeConvert(String email) {

        Optional<Member> member = getMember(email);

        if(getMember(member).getReportCount() >= 30) {
            getMember(member).memberBlackListType(BLACKLIST);

            MemberResponseDto memberResponseDto = getMemberResponseDto(getMember(member));

            return new ResponseEntity<>(memberResponseDto, OK);
        } else {
            throw new IllegalStateException("회원의 신고 수가 30개가 넘지 않아서 블랙리스트 타입을 지정할 수 없습니다.");
        }
    }

    /**
     * Optional 상태에서 객체 꺼내기
     * @param member
     * @return 회원 객체
     */
    private static Member getMember(Optional<Member> member) {
        return member.get();
    }

    /**
     * 회원 응답 Dto 로 변형
     * @param member
     * @return 회원 응답 Dto
     */
    private static MemberResponseDto getMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .member(member)
                .build();
    }

    /**
     * 회원 찾기
     * @param memberRequestDto
     * @return Optional Member
     */
    private Optional<Member> getMember(String email) {
        return ofNullable(memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));
    }
}
