package sejong.foodsns.service.member.login.jwt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.member.login.jwt.MemberLoginService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage.LOGOUT_SUCCESS;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberJwtLoginServiceImplTest {

    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private MemberCrudService memberCrudService;

    private final MemberRequestDto requestDto = MemberRequestDto.builder()
            .username("윤광오")
            .password("rhkddh77@A")
            .email("swager253@naver.com")
            .build();

    private MemberLoginDto loginDto;

    @BeforeEach
    void MemberInit() {
        loginDto = new MemberLoginDto("swager253@naver.com", "rhkddh77@A");
        memberCrudService.memberCreate(requestDto);
    }

    @Test
    @Order(0)
    @DisplayName("로그인 성공")
    void jwtLoginSuccess() throws JsonProcessingException {
        // given

        // when
        ResponseEntity<String> jwtLogin = memberLoginService.jwtLogin(loginDto);

        // then
        assertThat(jwtLogin.getBody()).isNotEmpty();
        System.out.println("accessToken : " + jwtLogin.getBody());
    }

    @Test
    @Order(1)
    @DisplayName("로그인 실패 이메일이 맞지 않을 때 예외")
    void jwtLoginFailedEmailNotMatch() throws JsonProcessingException {
        // given
        MemberLoginDto memberLoginDto = new MemberLoginDto("swager253@daum.net", "rhkddh77@A");

        // when

        // then
        assertThatThrownBy(() -> memberLoginService.jwtLogin(memberLoginDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(2)
    @DisplayName("로그인 실패 이메일은 맞지만 비밀번호가 틀렸을 때 예외")
    void jwtLoginFailedPasswordNotMatch() throws JsonProcessingException {
        // given
        MemberLoginDto memberLoginDto = new MemberLoginDto("swager253@naver.com", "alstngud77@A");

        // when

        // then
        assertThatThrownBy(() -> memberLoginService.jwtLogin(memberLoginDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(3)
    @DisplayName("로그아웃 성공")
    void jwtLogoutSuccess() throws JsonProcessingException {
        // given
        ResponseEntity<String> jwtLogin = memberLoginService.jwtLogin(loginDto);

        // when
        ResponseEntity<String> jwtLogout = memberLoginService.jwtLogout(loginDto.getEmail(), jwtLogin.getBody());

        // then
        assertThat(jwtLogout.getBody()).isEqualTo(LOGOUT_SUCCESS);
    }

}