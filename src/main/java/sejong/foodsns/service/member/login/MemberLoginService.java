package sejong.foodsns.service.member.login;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

@Service
public interface MemberLoginService {

    ResponseEntity<MemberResponseDto> login(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberResponseDto> logout(MemberRequestDto memberRequestDto);
}
