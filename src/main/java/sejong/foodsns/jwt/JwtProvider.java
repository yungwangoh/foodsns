package sejong.foodsns.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.token.TokenResponseDto;
import sejong.foodsns.exception.http.ForbiddenException;
import sejong.foodsns.service.redis.RedisService;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    
    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.expire_time.access}")
    private Long accessTokenExpireTime;

    @Value("${spring.jwt.expire_time.refresh}")
    private Long refreshTokenExpireTime;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public TokenResponseDto createTokenByLogin(MemberLoginDto memberLoginDto) throws JsonProcessingException{
        String accessToken = createAccessToken(memberLoginDto);
        String refreshToken = createRefreshToken(memberLoginDto);

        redisService.setValues(memberLoginDto.getEmail(), refreshToken, Duration.ofMillis(refreshTokenExpireTime));
        return new TokenResponseDto(accessToken);
    }

    public String createRefreshToken(MemberLoginDto memberLoginDto) throws JsonProcessingException {
        return createToken(memberLoginDto.getEmail(), refreshTokenExpireTime);
    }

    public String createAccessToken(MemberLoginDto memberLoginDto) throws JsonProcessingException {
        return createToken(memberLoginDto.getEmail(), accessTokenExpireTime);
    }

    public String createToken(String emailAccount, Long expireTime) throws JsonProcessingException {
        String objectCovertString = objectMapper.writeValueAsString(emailAccount);
        Claims claims = Jwts.claims().setSubject(objectCovertString);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public MemberLoginDto getLoginDto(String accessToken) throws JsonProcessingException {
        String subject = isValidToken(accessToken);
        if (subject == null) return null;
        return objectMapper.readValue(subject, MemberLoginDto.class);
    }

    public String isValidToken(String accessToken) {
        String subject;
        try {
            subject = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody().getSubject();
        } catch (ExpiredJwtException e) { // 토큰 만료 예외.
            return null;
        }
        return subject;
    }

    public TokenResponseDto reissueToken(MemberLoginDto memberLoginDto) throws JsonProcessingException, ForbiddenException {
        String getRefreshToken = redisService.getValues(memberLoginDto.getEmail());
        if(getRefreshToken.isEmpty())
            throw new ForbiddenException("인증 정보가 만료되었습니다.");

        String reissueAccessToken = createAccessToken(memberLoginDto);
        return new TokenResponseDto(reissueAccessToken);
    }


}
