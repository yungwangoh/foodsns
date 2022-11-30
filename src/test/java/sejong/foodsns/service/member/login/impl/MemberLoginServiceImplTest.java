package sejong.foodsns.service.member.login.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.member.login.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberLoginServiceImplTest {

    @Autowired
    private MemberLoginServiceImpl memberLoginService;
    @Autowired
    private MemberCrudService memberCrudService;
    private MemberLoginDto memberLoginDto;
    private static HttpServletRequest request;
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void memberLoginInit() {
        memberLoginDto = new MemberLoginDto("윤광오", "swager253@naver.com", "rhkddh77@A");
        memberRequestDto = getMemberRequestDto("윤광오", "swager253@naver.com", "rhkddh77@A");
    }

    @BeforeAll
    static void httpServletRequestInit() {
        request = new MockHttpServletRequest();
    }

    @Test
    @Order(0)
    @DisplayName("회원 로그인 성공")
    void memberLoginSuccess() {
        // given
        memberCrudService.memberCreate(memberRequestDto);

        // when
        ResponseEntity<String> loginMember = memberLoginService.login(memberLoginDto, request);

        // then
        assertThat(loginMember.getBody()).isEqualTo(LOGIN_SUCCESS);
    }

    @Test
    @Order(2)
    @DisplayName("회원 로그아웃 성공")
    void memberLogoutSuccess() {
        // given

        // when
        ResponseEntity<String> logoutMember = memberLoginService.logout(request);

        // then
        assertThat(logoutMember.getBody()).isEqualTo(LOGOUT_SUCCESS);
    }

    private MemberRequestDto getMemberRequestDto(String username, String email, String password) {
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        return memberRequestDto;
    }
}