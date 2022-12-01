package sejong.foodsns.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.service.redis.RedisService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;
    private MemberLoginDto memberLoginDto;

    @Value("${spring.jwt.expire_time.access}")
    private Long accessTokenExpireTime;

    @Value("${spring.jwt.expire_time.refresh}")
    private Long refreshTokenExpireTime;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void memberLoginInit() {
        memberLoginDto = new MemberLoginDto("윤광오", "swager253@naver.com", "rhkddh77@A");
    }

    @Test
    @DisplayName("토큰 발급 성공")
    void createTokenSuccess() throws JsonProcessingException {
        // given

        // when
        String accessToken = jwtProvider.createToken(memberLoginDto, accessTokenExpireTime);

        // then
        assertThat(accessToken).isNotNull();
        System.out.println("accessToken : " + accessToken);
    }
}