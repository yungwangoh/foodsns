package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberBlackListServiceImplTest {

    @Autowired
    private MemberBlackListService memberBlackListService;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private MemberBusinessService memberBusinessService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BlackListRepository blackListRepository;

    @AfterEach
    void initDB() {
        blackListRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("블랙리스트 등록 -> 신고 수가 30개 이상 -> 패널티 개수가 3개면 등록")
    void blackListMemberCreate() {
        // given
        String reason = "악의 적인 댓글";

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@zzz.com")
                .password("qwer1234@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
        reportCount(memberRequestDto, 30);
        memberBusinessService.memberBlackListTypeConvert(memberRequestDto.getEmail());

        // when
        ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(reason, memberRequestDto.getEmail());

        // then
        assertThat(blackListMemberCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(getBody(blackListMemberCreate).getReason()).isEqualTo(reason);
        assertThat(getBody(blackListMemberCreate)).isNotNull();
    }

    @Test
    @DisplayName("블랙리스트 등록 실패 -> 30개가 넘지 않은 신고 수")
    void blackListMemberDoNotExceedThirty() {
        // given
        String reason = "악의 적인 댓글";

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@zzz.com")
                .password("qwer1234@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
        reportCount(memberRequestDto, 20);

        // when

        // then

        // 블랙리스트 타입으로 변하지 않는다.
        assertThatThrownBy(() -> memberBusinessService.memberBlackListTypeConvert(memberRequestDto.getEmail()))
                .isInstanceOf(IllegalStateException.class);

        // 블랙리스트 타입으로 바뀌지 않으면 등록이 되지 않는다.
        assertThatThrownBy(() -> memberBlackListService.blackListMemberCreate(reason, memberRequestDto.getEmail()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원이 블랙리스트가 아닐 경우 등록 못함")
    void memberIsNotBlackListDoNotCreate() {
        // given -> 신고 수가 30개인데 블랙리스트 타입을 변경하지 않은 사례.
        String reason = "악의 적인 댓글";

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@zzz.com")
                .password("qwer1234@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
        reportCount(memberRequestDto, 30);

        // when

        // then
        assertThatThrownBy(() -> memberBlackListService.blackListMemberCreate(reason, memberRequestDto.getEmail()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("블랙리스트 회원 찾기")
    void blackListMemberSearch() {
        // given
        String reason = "악의 적인 댓글";

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@zzz.com")
                .password("qwer1234@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
        reportCount(memberRequestDto, 30);
        memberBusinessService.memberBlackListTypeConvert(memberRequestDto.getEmail());

        ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(reason, memberRequestDto.getEmail());

        // when
        ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne =
                memberBlackListService.blackListMemberFindOne(getBody(blackListMemberCreate).getId());

        // then
        assertThat(getBody(blackListMemberFindOne).getReason()).isEqualTo(reason);
    }

    @Test
    @DisplayName("블랙리스트인 회원 상세 조회")
    void blackListMemberDetailSearch() {
        // given
        String reason = "악의 적인 댓글";

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@zzz.com")
                .password("qwer1234@A")
                .build();

        memberCrudService.memberCreate(memberRequestDto);
        reportCount(memberRequestDto, 30);
        ResponseEntity<MemberResponseDto> blackListTypeConvert =
                memberBusinessService.memberBlackListTypeConvert(memberRequestDto.getEmail());

        ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(reason, memberRequestDto.getEmail());

        // when
        ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch =
                memberBlackListService.blackListMemberDetailSearch(getBody(blackListMemberCreate).getId());

        // then
        assertThat(getBlackListDetailDto(blackListMemberDetailSearch).getMemberResponseDto())
                .isEqualTo(blackListTypeConvert.getBody());

    }

    private static Optional<MemberResponseDto> getMemberCreateBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return memberCreate.getBody();
    }

    private static MemberBlackListDetailDto getBlackListDetailDto(ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch) {
        return blackListMemberDetailSearch.getBody();
    }

    private static MemberBlackListResponseDto getBody(ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate) {
        return blackListMemberCreate.getBody();
    }

    private void reportCount(MemberRequestDto memberRequestDto, int num) {
        for(int i = 0; i < num; i++) memberBusinessService.memberReportCount(memberRequestDto.getEmail());
    }
}