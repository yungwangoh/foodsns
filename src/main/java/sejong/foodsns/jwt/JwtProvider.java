package sejong.foodsns.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.token.TokenResponseDto;
import sejong.foodsns.exception.http.ForbiddenException;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.redis.RedisService;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final MemberCrudService memberCrudService;
    @Value("${spring.jwt.key}")
    private String key;
    @Value("${spring.jwt.blackList}")
    private String blackList;
    @Value("${spring.jwt.expire_time.access}")
    private Long accessTokenExpireTime;
    @Value("${spring.jwt.expire_time.refresh}")
    private Long refreshTokenExpireTime;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    /**
     * 로그인을 위한 토큰 생성. 리프레시 토큰은 Redis에 저장 엑세스는 클라이언트로 보냄
     * @param email
     * @return TokenResponseDto
     * @throws JsonProcessingException
     */
    public TokenResponseDto createTokenByLogin(String email) throws JsonProcessingException{
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken(email);

        redisService.setValues(email, refreshToken, Duration.ofMillis(refreshTokenExpireTime));
        return new TokenResponseDto(accessToken);
    }

    /**
     * 리프레시 토큰 생성
     * @param email
     * @return 리프레시 토큰
     * @throws JsonProcessingException
     */
    public String createRefreshToken(String email) throws JsonProcessingException {
        return createToken(email, refreshTokenExpireTime);
    }

    /**
     * 엑세스 토큰 생성
     * @param email
     * @return 엑세스 토큰
     * @throws JsonProcessingException
     */
    public String createAccessToken(String email) throws JsonProcessingException {
        return createToken(email, accessTokenExpireTime);
    }

    /**
     * 토큰 생성
     * @param emailAccount 이메일 계정
     * @param expireTime 만료 기간
     * @return JWT Token
     * @throws JsonProcessingException
     */
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

    /**
     * 로그인 회원 정보
     * @param accessToken
     * @return 회원 정보 객체
     * @throws JsonProcessingException
     */
    public MemberResponseDto getLoginDto(String accessToken) throws JsonProcessingException {
        String subject = isValidToken(accessToken);
        if (subject == null) return null;
        return objectMapper.readValue(subject, MemberResponseDto.class);
    }

    /**
     * 로그아웃
     * @param email
     * @param accessToken
     */
    public void logout(String email, String accessToken) {
        long expireTime = getTokenExpireTime(accessToken).getTime() - new Date().getTime();
        redisService.setValues(blackList + accessToken, email, Duration.ofMillis(expireTime));
        redisService.deleteValues(email);
    }

    /**
     * 토큰 유효성 검사 bool type
     * @param accessToken
     * @return 유효함 true, 유효하지 않음 false
     */
    public boolean isValidTokenCheck(String accessToken) {

        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
            log.info("[expireTime] = {}", claims.getExpiration());
            log.info("[subject] = {}", claims.getSubject());
            return true;
        } catch (JwtException | NullPointerException e) {
            log.error("Token Error");
            return false;
        }
    }

    // barer split get jwt token
    public String getFormatToken(String accessToken) {
        return accessToken.split(" ")[1];
    }

    /**
     * 토큰 유효성 검사
     * @param accessToken
     * @return subject
     */
    public String isValidToken(String accessToken) {
        String subject;
        try {
            subject = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody().getSubject();
        } catch (ExpiredJwtException e) { // 토큰 만료 예외.
            return null;
        }
        return subject;
    }

    /**
     * 토큰 재발급
     * @param email
     * @return 재발급 토큰
     * @throws JsonProcessingException
     * @throws ForbiddenException 리프레시 토큰이 Redis에 없을 경우 예외
     */
    public TokenResponseDto reissueToken(String email) throws JsonProcessingException, ForbiddenException {
        String getRefreshToken = redisService.getValues(email);
        if(getRefreshToken.isEmpty())
            throw new ForbiddenException("인증 정보가 만료되었습니다.");

        String reissueAccessToken = createAccessToken(email);
        return new TokenResponseDto(reissueAccessToken);
    }

    private static Optional<MemberResponseDto> getBody(ResponseEntity<Optional<MemberResponseDto>> member) {
        return member.getBody();
    }

    private Date getTokenExpireTime(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
    }
}
