package sejong.foodsns.service.member.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
@Rollback(value = false)
class MemberCrudServiceImplTest {

    @Autowired
    private MemberCrudServiceImpl memberCrudService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    private MemberRequestDto memberRequestDto;
    private MemberResponseDto memberResponseDto;

    @BeforeEach
    @DisplayName("회원 요청 정보 초기화")
    void memberRequestInit() {
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
    @DisplayName("DB 저장된 영속성 데이터 제거")
    void persistDeleteInit() {
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
            assertTrue(getFindMemberBody(findMember).isPresent());
        }

        @Test
        @Order(4)
        @DisplayName("회원 목록")
        void memberList() {

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
            ResponseEntity<Optional<MemberResponseDto>> deleteMember = memberCrudService.findMember(memberRequestDto);
            assertFalse(getFindMemberBody(deleteMember).isPresent());
        }
    }

    @Nested
    @DisplayName("실패")
    class serviceFail {

    }

    private MemberResponseDto getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return getFindMemberBody(memberCreate).get();
    }

    private Optional<MemberResponseDto> getFindMemberBody(ResponseEntity<Optional<MemberResponseDto>> findMember) {
        return findMember.getBody();
    }
}