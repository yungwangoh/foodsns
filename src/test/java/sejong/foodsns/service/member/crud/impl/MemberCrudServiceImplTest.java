package sejong.foodsns.service.member.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
class MemberCrudServiceImplTest {

    @Autowired
    private MemberCrudServiceImpl memberCrudService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    private static MemberRequestDto memberRequestDto;
    private MemberResponseDto memberResponseDto;

    @BeforeAll
    @DisplayName("회원 요청 정보 초기화")
    static void memberRequestInit() {

        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    @BeforeEach
    @DisplayName("회원 응답 정보 초기화")
    void memberResponseInit() {
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        memberResponseDto = MemberResponseDto.builder()
                .member(member)
                .build();
    }

    @AfterEach
    @DisplayName("DB 영속성 데이터 모두 삭제")
    void persistAllDelete() {
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("서비스 성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class serviceSuccess {

        @Test
        @Order(0)
        @DisplayName("회원 가입")
        void memberCreate() {
            //given

            //when
            ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);

            //then
            assertThat(memberCreate.getStatusCode()).isEqualTo(CREATED);
            assertThat(getBody(memberCreate).getUsername()).isEqualTo(memberResponseDto.getUsername());
            assertThat(getBody(memberCreate).getEmail()).isEqualTo(memberResponseDto.getEmail());
            assertThat(getBody(memberCreate).getMemberType()).isEqualTo(memberResponseDto.getMemberType());
            assertThat(getBody(memberCreate).getMemberRank()).isEqualTo(memberResponseDto.getMemberRank());
        }

        @Test
        @Order(1)
        @DisplayName("회원 비밀번호 수정")
        void memberPasswordUpdate() {
            // given
            String tempPassword = "rhkddh77@A";

            // when
            ResponseEntity<Optional<MemberResponseDto>> passwordUpdate = memberCrudService.memberPasswordUpdate(memberRequestDto, tempPassword);

            // then
            assertThat(passwordUpdate.getStatusCode()).isEqualTo(OK);
            assertTrue(passwordEncoder.matches(tempPassword, getBody(passwordUpdate).getPassword()));
        }

        @Test
        @Order(2)
        @DisplayName("회원 닉네임 수정")
        void memberNameUpdate() {
            // given
            String username = "하윤";

            // when
            ResponseEntity<Optional<MemberResponseDto>> nameUpdate = memberCrudService.memberNameUpdate(memberRequestDto, "하윤");

            //then
            assertThat(nameUpdate.getStatusCode()).isEqualTo(OK);
            assertThat(getBody(nameUpdate).getUsername()).isEqualTo(username);
        }

        @Test
        @Order(3)
        @DisplayName("회원 찾기")
        void findMember() {
            // given

            // when
            ResponseEntity<Optional<MemberResponseDto>> findMember = memberCrudService.findMember(memberRequestDto);

            // then
            assertThat(findMember.getStatusCode()).isEqualTo(OK);
            assertTrue(getFindMemberBody(findMember).isPresent());
        }

        @Test
        @Order(4)
        @DisplayName("회원 목록")
        void memberList() {
            // given

            // when
            ResponseEntity<Optional<List<MemberResponseDto>>> memberList = memberCrudService.memberList();
            long count = memberRepository.count();

            // then
            assertThat(memberList.getStatusCode()).isEqualTo(OK);
            assertThat(getMemberList(memberList).size()).isEqualTo(count);
        }

        @Test
        @Order(5)
        @DisplayName("회원 탈퇴")
        void memberDelete() {
            // given
            ResponseEntity<Optional<MemberResponseDto>> findMember = memberCrudService.findMember(memberRequestDto);
            assertTrue(getFindMemberBody(findMember).isPresent());

            // when
            memberCrudService.memberDelete(memberRequestDto);

            // then
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<MemberResponseDto>> deleteMember = memberCrudService.memberDelete(memberRequestDto);

                assertThat(deleteMember.getStatusCode()).isEqualTo(OK);
            }).isInstanceOf(IllegalArgumentException.class);

        }
    }

    @Nested
    @DisplayName("실패")
    class serviceFail {

    }

    private List<MemberResponseDto> getMemberList(ResponseEntity<Optional<List<MemberResponseDto>>> memberList) {
        return memberList.getBody().get();
    }

    private MemberResponseDto getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return getFindMemberBody(memberCreate).get();
    }

    private Optional<MemberResponseDto> getFindMemberBody(ResponseEntity<Optional<MemberResponseDto>> findMember) {
        return findMember.getBody();
    }
}