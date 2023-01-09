package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberBusinessServiceImplTest {

    @Autowired
    private MemberBusinessService memberBusinessService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberCrudService memberCrudService;
    private static MemberRequestDto memberRequestDto;

    @BeforeEach
    void init() {
        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
    }

    @AfterEach
    void initDB() {
        memberRepository.deleteAll();
    }

    @Test
    @Order(0)
    @DisplayName("회원 랭크 업")
    void memberBusinessRankUp() {
        // given
        Optional<Member> member = getMember();

        // 회원은 실버 등급
        member.get().memberRecommendCount(30);
        memberRepository.save(member.get());

        // when
        ResponseEntity<MemberResponseDto> memberRankService = memberBusinessService.memberRankService(memberRequestDto);
        MemberResponseDto memberResponseDto = getBody(memberRankService);

        // then
        assertThat(memberResponseDto.getMemberRank()).isEqualTo(MemberRank.SILVER);
    }

    @Test
    @Order(1)
    @DisplayName("회원 추천수가 음수일 때 예외 테스트")
    void memberRecommendCountNegativeNum() {
        // given
        Optional<Member> member = getMember();

        // when

        // then
        // 부분 테스트를 하여 예외를 확인 가능.
        assertThatThrownBy(() -> member.get().memberRankUp(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(2)
    @DisplayName("회원 신고 수 증가")
    void memberReportCountUp() {
        // given

        // when
        ResponseEntity<MemberResponseDto> memberReportCount = memberBusinessService.memberReportCount(memberRequestDto);

        // then
        assertThat(getBody(memberReportCount).getReportCount()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @DisplayName("회원 패널티 수 증가")
    void memberPenaltyCountUp() {
        // given

        // when
        ResponseEntity<MemberResponseDto> memberBlackListPenaltyCount =
                memberBusinessService.memberBlackListPenaltyCount(memberRequestDto);

        // then
        assertThat(getBody(memberBlackListPenaltyCount).getPenaltyCount()).isEqualTo(1);
    }

    @Test
    @Order(4)
    @DisplayName("회원")
    void member() {

    }

    private Optional<Member> getMember() {
        return memberRepository.findMemberByEmail("swager253@naver.com");
    }

    private MemberResponseDto getBody(ResponseEntity<MemberResponseDto> memberReportCount) {
        return memberReportCount.getBody();
    }
}