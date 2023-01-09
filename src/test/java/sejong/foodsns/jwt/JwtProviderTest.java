package sejong.foodsns.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.token.TokenResponseDto;
import sejong.foodsns.exception.http.ForbiddenException;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.redis.RedisService;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MemberCrudService memberCrudService;
    private MemberLoginDto memberLoginDto;

    @Value("${spring.jwt.expire_time.access}")
    private Long accessTokenExpireTime;

    @Value("${spring.jwt.expire_time.refresh}")
    private Long refreshTokenExpireTime;

    @BeforeEach
    void memberLoginInit() {
        memberLoginDto = new MemberLoginDto("swager253@naver.com", "rhkddh77@A");
    }

    @BeforeEach
    void memberCreateInit() {
        MemberRequestDto memberRequestDto =
                getMemberRequestDto("윤광오", "swager253@naver.com", "rhkddh77@A");

        memberCrudService.memberCreate(memberRequestDto);
    }

    @AfterEach
    void memberDeleteInit() {
        MemberRequestDto memberRequestDto =
                getMemberRequestDto("윤광오", "swager253@naver.com", "rhkddh77@A");

        memberCrudService.memberDelete(memberRequestDto);
    }

    private MemberRequestDto getMemberRequestDto(String username, String email, String password) {
        return MemberRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    @DisplayName("토큰 발급 성공")
    void createTokenSuccess() throws JsonProcessingException {
        // given

        // when
        String accessToken = jwtProvider.createToken(memberLoginDto.getEmail(), accessTokenExpireTime);

        // then
        assertThat(accessToken).isNotNull();
        System.out.println("accessToken : " + accessToken);
    }

    @Test
    @DisplayName("엑세스 토큰 발급 정보 맞는지 확인")
    void accessTokenMatchInfo() throws JsonProcessingException {
        // given
        String accessToken = jwtProvider.createAccessToken(memberLoginDto.getEmail());

        // when
        MemberResponseDto memberResponseDto = jwtProvider.getLoginDto(accessToken);

        // then
        assertThat(memberResponseDto).isNotNull();
    }

    @Test
    @DisplayName("만료된 토큰을 넣었을 경우 반환 값 NULL 확인")
    void ExpiredAccessTokenNotMatchedInfo() throws JsonProcessingException {
        // given
        String expiredToken = jwtProvider.createToken(memberLoginDto.getEmail(), 0L);

        // when
        MemberResponseDto memberResponseDto = jwtProvider.getLoginDto(expiredToken);

        // then
        assertThat(memberResponseDto).isNull();
    }

    @Test
    @DisplayName("리프레시 토큰 Redis 에 저장이 잘 되었는지 확인")
    void checkRefreshTokenSaveToRedis() throws JsonProcessingException {
        // given
        jwtProvider.createTokenByLogin(memberLoginDto.getEmail());

        // when
        String refreshToken = redisService.getValues(memberLoginDto.getEmail());

        // then
        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("토큰 로그인 Redis -> refreshToken")
    void tokenLogin() throws JsonProcessingException{
        // given

        // when
        TokenResponseDto tokenByLogin = jwtProvider.createTokenByLogin(memberLoginDto.getEmail());
        String refreshToken = redisService.getValues(memberLoginDto.getEmail());

        // then
        assertFalse(tokenByLogin.getAccessToken().isEmpty());
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void tokenValidationCheck() throws JsonProcessingException {
        // given
        String expiredToken = jwtProvider.createToken(memberLoginDto.getEmail(), 0L);
        String accessToken = jwtProvider.createToken(memberLoginDto.getEmail(), accessTokenExpireTime);

        // when
        String expiredValidToken = jwtProvider.isValidToken(expiredToken);
        String accessValidToken = jwtProvider.isValidToken(accessToken);

        // then
        assertThat(expiredValidToken).isNull();
        assertThat(accessValidToken).isNotNull();
    }

    @Test
    @DisplayName("토큰 재발급")
    void tokenReIssue() throws JsonProcessingException, ForbiddenException {
        // given
        TokenResponseDto tokenByLogin = jwtProvider.createTokenByLogin(memberLoginDto.getEmail());

        // when

        // 만료 토큰 검증하고 로그인 정보 반환
        MemberResponseDto memberResponseDto = jwtProvider.getLoginDto(tokenByLogin.getAccessToken());

        // 반환된 로그인 정보를 토대로 재발급.
        TokenResponseDto tokenResponseDto = jwtProvider.reissueToken(memberResponseDto.getEmail());

        // then
        assertThat(tokenResponseDto.getAccessToken()).isNotNull();
        System.out.println("AccessToken = " + tokenResponseDto.getAccessToken());
    }

    @Test
    @DisplayName("토큰 HTTP 헤더 전송")
    void sendSetTokenHttpHeader() throws JsonProcessingException {
        // given
        HttpServletResponse response = new MockHttpServletResponse();
        TokenResponseDto tokenByLogin = jwtProvider.createTokenByLogin(memberLoginDto.getEmail());

        // when
        response.setHeader("X-AUTH-TOKEN", tokenByLogin.getAccessToken());

        // then
        String token = response.getHeader("X-AUTH-TOKEN");
        assertThat(tokenByLogin.getAccessToken()).isEqualTo(token);
    }
}