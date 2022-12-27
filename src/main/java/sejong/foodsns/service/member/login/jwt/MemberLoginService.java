package sejong.foodsns.service.member.login.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.login.MemberLoginDto;

public interface MemberLoginService {
    ResponseEntity<String> jwtLogin(MemberLoginDto loginDto) throws JsonProcessingException;
    ResponseEntity<String> jwtLogout(String email, String accessToken);
}
