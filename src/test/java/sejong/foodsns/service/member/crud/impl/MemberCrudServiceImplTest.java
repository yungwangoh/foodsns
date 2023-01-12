package sejong.foodsns.service.member.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    private MemberResponseDto memberResponseDto;

    @BeforeEach
    void memberResponseInit() {
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        memberResponseDto = MemberResponseDto.builder()
                .member(member)
                .build();
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
            MemberRequestDto memberRequestDto = getMemberRequestDto();

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
        @DisplayName("회원 찾기")
        void findMember() {
            // given
            MemberRequestDto memberRequestDto = getMemberRequestDto();
            memberCrudService.memberCreate(memberRequestDto);

            // when
            ResponseEntity<Optional<MemberResponseDto>> findMember = memberCrudService.findMember(memberRequestDto.getEmail());

            // then
            assertThat(findMember.getStatusCode()).isEqualTo(OK);
            assertTrue(getFindMemberBody(findMember).isPresent());
        }

        @Test
        @Order(2)
        @DisplayName("회원 목록")
        void memberList() {
            // given
            List<ResponseEntity<Optional<MemberResponseDto>>> list = new ArrayList<>();
            list.add(memberCrudService.memberCreate(getMemberRequestDto()));
            list.add(memberCrudService.memberCreate(getMemberRequestDtoTwo()));
            list.add(memberCrudService.memberCreate(getMemberRequestDtoThree()));

            // when
            ResponseEntity<Optional<List<MemberResponseDto>>> memberList = memberCrudService.memberList();

            // then
            assertThat(memberList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getMemberResponseDtos(memberList).size());
        }

        @Test
        @Order(3)
        @DisplayName("회원 비밀번호 수정")
        void memberPasswordUpdate() {
            // given
            String tempPassword = "alstngud77@A";
            MemberRequestDto memberRequestDto = getMemberRequestDto();
            ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);

            // when
            ResponseEntity<Optional<MemberResponseDto>> passwordUpdate =
                    memberCrudService.memberPasswordUpdate(memberRequestDto.getEmail(), tempPassword);

            Optional<Member> member = memberRepository.findByEmail(getBody(passwordUpdate).getEmail());

            // then
            assertThat(passwordUpdate.getStatusCode()).isEqualTo(OK);
            assertTrue(passwordEncoder.matches(tempPassword, getMember(member).getPassword()));
        }

        @Test
        @Order(4)
        @DisplayName("회원 닉네임 수정")
        void memberNameUpdate() {
            // given
            String username = "하윤";
            MemberRequestDto memberRequestDto = getMemberRequestDto();
            memberCrudService.memberCreate(memberRequestDto);

            // when
            ResponseEntity<Optional<MemberResponseDto>> nameUpdate =
                    memberCrudService.memberNameUpdate(memberRequestDto.getEmail(), "하윤");

            Optional<Member> member = memberRepository.findByEmail(getBody(nameUpdate).getEmail());

            //then
            assertThat(nameUpdate.getStatusCode()).isEqualTo(OK);
            assertThat(getMember(member).getUsername()).isEqualTo(username);
        }

        @Test
        @Order(5)
        @DisplayName("회원 탈퇴")
        void memberDelete() {
            // given
            MemberRequestDto memberRequestDto = getMemberRequestDto();
            memberCrudService.memberCreate(memberRequestDto);

            // when
            memberCrudService.memberDelete(memberRequestDto);

            // then
            // 찾으려는 회원이 없어야한다.
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<MemberResponseDto>> member =
                        memberCrudService.findMember(memberRequestDto.getEmail());

                assertThat(member.getStatusCode()).isEqualTo(NO_CONTENT);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @AfterEach
        void deleteInit() {
            memberRepository.deleteAll();
        }

        private List<MemberResponseDto> getMemberResponseDtos(ResponseEntity<Optional<List<MemberResponseDto>>> memberList) {
            return memberList.getBody().get();
        }
    }

    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    @Nested
    @DisplayName("서비스 실패")
    class serviceFail {

        @Test
        @DisplayName("이름, 이메일 중복 -> 회원 가입 실패")
        void memberDuplicatedValidationFail() {
            // given
            MemberRequestDto memberRequestDto = getMemberRequestDto();
            memberCrudService.memberCreate(memberRequestDto);

            // when
            Boolean memberNameExistValidation = memberCrudService.memberNameExistValidation(memberRequestDto);
            Boolean memberEmailExistValidation = memberCrudService.memberEmailExistValidation(memberRequestDto);

            // then
            // true -> 중복
            assertTrue(memberNameExistValidation);
            assertTrue(memberEmailExistValidation);
        }

        @Test
        @DisplayName("찾으려는 회원이 존재하지 않을때 예외")
        void memberFindException() {
            // given
            MemberRequestDto memberRequestDto = getMemberRequestDto();

            // when
            memberCrudService.memberCreate(memberRequestDto);

            // then
            assertThatThrownBy(() -> memberCrudService.findMember(getMemberRequestDtoTwo().getEmail()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @AfterEach
        void deleteInit() {
            memberRepository.deleteAll();
        }
    }

    private MemberRequestDto getMemberRequestDto() {
        return MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    private MemberRequestDto getMemberRequestDtoTwo() {
        return MemberRequestDto.builder()
                .username("하윤")
                .email("qkfks1234@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    private MemberRequestDto getMemberRequestDtoThree() {
        return MemberRequestDto.builder()
                .username("윤민수")
                .email("alstngud77@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    private MemberResponseDto getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return getFindMemberBody(memberCreate).get();
    }

    private Optional<MemberResponseDto> getFindMemberBody(ResponseEntity<Optional<MemberResponseDto>> findMember) {
        return findMember.getBody();
    }
}