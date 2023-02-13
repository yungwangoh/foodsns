package sejong.foodsns.controller.member.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.log.error.ErrorResult;
import sejong.foodsns.service.member.login.jwt.MemberLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 (JWT) 토큰 방식 로그인. 엑세스 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping("/member/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberLoginDto memberLoginDto) throws JsonProcessingException {

        ResponseEntity<String> jwtLogin = memberLoginService.jwtLogin(memberLoginDto);

        return new ResponseEntity<>(jwtLogin.getBody(), jwtLogin.getStatusCode());
    }

    @Operation(summary = "로그아웃", description = "이메일과 HTTP request로 로그아웃 (Redis 리프레시 토큰 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/member/logout")
    public ResponseEntity<String> logout(@RequestParam("email") String email, HttpServletRequest request) {

        String accessToken = request.getHeader("X-AUTH-TOKEN");

        ResponseEntity<String> jwtLogout = memberLoginService.jwtLogout(email, accessToken);

        return new ResponseEntity<>(jwtLogout.getBody(), jwtLogout.getStatusCode());
    }
}
