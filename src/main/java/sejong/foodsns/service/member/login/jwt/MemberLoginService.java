package sejong.foodsns.service.member.login.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.login.MemberLoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberLoginService {
    ResponseEntity<String> jwtLogin(MemberLoginDto loginDto) throws JsonProcessingException;
    ResponseEntity<String> jwtLogout(String email, String accessToken);
}
