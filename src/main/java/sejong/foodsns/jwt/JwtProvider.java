package sejong.foodsns.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.token.TokenResponseDto;
import sejong.foodsns.exception.http.ForbiddenException;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.redis.RedisService;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final MemberCrudService memberCrudService;
    
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

    public TokenResponseDto createTokenByLogin(String email) throws JsonProcessingException{
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken(email);

        redisService.setValues(email, refreshToken, Duration.ofMillis(refreshTokenExpireTime));
        return new TokenResponseDto(accessToken);
    }

    public String createRefreshToken(String email) throws JsonProcessingException {
        return createToken(email, refreshTokenExpireTime);
    }

    public String createAccessToken(String email) throws JsonProcessingException {
        return createToken(email, accessTokenExpireTime);
    }

    public String createToken(String emailAccount, Long expireTime) throws JsonProcessingException {

        ResponseEntity<Optional<MemberResponseDto>> member = memberCrudService.findMember(emailAccount);

        String objectCovertString = objectMapper.writeValueAsString(getBody(member).get());
        Claims claims = Jwts.claims().setSubject(objectCovertString);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public MemberResponseDto getLoginDto(String accessToken) throws JsonProcessingException {
        String subject = isValidToken(accessToken);
        if (subject == null) return null;
        return objectMapper.readValue(subject, MemberResponseDto.class);
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

    public TokenResponseDto reissueToken(String email) throws JsonProcessingException, ForbiddenException {
        String getRefreshToken = redisService.getValues(email);
        if(getRefreshToken.isEmpty())
            throw new ForbiddenException("인증 정보가 만료되었습니다.");

        String reissueAccessToken = createAccessToken(email);
        return new TokenResponseDto(reissueAccessToken);
    }

    private Optional<MemberResponseDto> getBody(ResponseEntity<Optional<MemberResponseDto>> member) {
        return member.getBody();
    }
}
