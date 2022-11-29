package sejong.foodsns.service.member.login;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.login.MemberLogoutDto;

import javax.servlet.http.HttpServletRequest;

public interface MemberLoginService {

    ResponseEntity<MemberResponseDto> login(MemberLoginDto memberLoginDto);

    ResponseEntity<MemberLogoutDto> logout(HttpServletRequest request);
}
