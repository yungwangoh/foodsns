package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;
import sejong.foodsns.service.member.business.MemberBusinessService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberBlackListServiceImplTest {

    @Autowired
    private MemberBlackListService memberBlackListService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReportMemberRepository reportMemberRepository;
    @Autowired
    private MemberBusinessService memberBusinessService;
    private static Member member;
    private static MemberRequestDto memberRequestDto;
    private static ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate;

    @BeforeAll
    static void init() {
         member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);

        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();

        for(int i = 0; i < 30; i++) {
            member.reportCount();
        }
    }

/*    @AfterEach
    void initDB() {
        reportMemberRepository.deleteAll();
        memberRepository.deleteAll();
    }*/

    @Test
    @Order(0)
    @DisplayName("블랙리스트 등록")
    void blackListMemberCreate() {
        // given
        ReportMember reportSave = testReportMemberInit();

        String reason = "악의 적인 댓글";
        MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = getMemberBlackListCreateRequestDto(reason, reportSave);

        // when
        blackListMemberCreate = memberBlackListService.blackListMemberCreate(memberBlackListCreateRequestDto);

        // then
        assertThat(getMemberBlackListResponseDto(blackListMemberCreate).getReason()).isEqualTo(reason);
        assertThat(blackListMemberCreate.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    @Order(1)
    @DisplayName("블랙리스트 회원 조회")
    void blackListMemberSearch() {
        // given
        String reason = "악의 적인 댓글";
        MemberBlackListRequestDto memberBlackListRequestDto =
                getMemberBlackListRequestDto(getMemberBlackListResponseDto(blackListMemberCreate));

        // when
        ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberFindOne =
                memberBlackListService.blackListMemberFindOne(memberBlackListRequestDto);

        // then
        assertThat(getMemberBlackListResponseDto(blackListMemberFindOne).getReason()).isEqualTo(reason);
        assertThat(blackListMemberFindOne.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @Order(2)
    @DisplayName("블랙리스트 회원 목록")
    void blackListMemberList() {
        // given

        // when
        ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList =
                memberBlackListService.blackListMemberList();

        // then (블랙리스트 회원이 한명 등록 되었으므로 목록의 size 는 1이 나와야한다.)
        assertThat(blackListMemberList.getBody().get().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @DisplayName("블랙리스트 회원 삭제 (실제 회원이 삭제 블랙리스트 DB의 저장된 회원은 삭제가 되지 않는다.)")
    void blackListMemberDelete() {
        // given
        MemberBlackListRequestDto memberBlackListRequestDto =
                getMemberBlackListRequestDto(getMemberBlackListResponseDto(blackListMemberCreate));

        // when
        ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberDelete =
                memberBlackListService.blackListMemberTypeChange(memberBlackListRequestDto);

        // then (삭제된 블랙리스트 회원의 정보와 회원의 블랙리스트 타입 확인)
        assertThat(blackListMemberDelete.getBody().get().getReason()).isEqualTo(memberBlackListRequestDto.getReason());
        assertThat(getMember(blackListMemberDelete).getMemberType()).isEqualTo(BLACKLIST);
    }

    private MemberBlackListCreateRequestDto getMemberBlackListCreateRequestDto(String reason, ReportMember reportMember) {
        return MemberBlackListCreateRequestDto.builder()
                .id(reportMember.getId())
                .reason(reason)
                .reportMember(reportMember)
                .build();
    }

    private MemberBlackListRequestDto getMemberBlackListRequestDto(MemberBlackListResponseDto memberBlackListResponseDto) {
        return MemberBlackListRequestDto.builder()
                .id(memberBlackListResponseDto.getId())
                .reason(memberBlackListResponseDto.getReason())
                .reportMember(memberBlackListResponseDto.getReportMember())
                .build();
    }

    private MemberBlackListResponseDto getMemberBlackListResponseDto(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMember) {
        return blackListMember.getBody().get();
    }

    private Optional<MemberBlackListResponseDto> getMemberBlackListBody(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMember) {
        return blackListMember.getBody();
    }

    private Member getMember(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberDelete) {
        return blackListMemberDelete.getBody().get().getReportMember().getMember();
    }

    private ReportMember testReportMemberInit() {
        Member save = memberRepository.save(member);
        ReportMember reportMember = ReportMember.builder()
                .member(save)
                .build();
        memberBusinessService.memberBlackListPenaltyCount(memberRequestDto);
        return reportMemberRepository.save(reportMember);
    }
}