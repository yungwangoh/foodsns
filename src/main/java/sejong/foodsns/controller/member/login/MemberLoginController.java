package sejong.foodsns.controller.member.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.service.member.login.jwt.MemberLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    @PostMapping("/member/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberLoginDto memberLoginDto) throws JsonProcessingException {

        ResponseEntity<String> jwtLogin = memberLoginService.jwtLogin(memberLoginDto);

        return new ResponseEntity<>(jwtLogin.getBody(), jwtLogin.getStatusCode());
    }

    @GetMapping("/member/logout")
    public ResponseEntity<String> logout(@RequestParam("email") String email, HttpServletRequest request) {

        String accessToken = request.getHeader("X-AUTH-TOKEN");

        ResponseEntity<String> jwtLogout = memberLoginService.jwtLogout(email, accessToken);

        return new ResponseEntity<>(jwtLogout.getBody(), jwtLogout.getStatusCode());
    }
}
