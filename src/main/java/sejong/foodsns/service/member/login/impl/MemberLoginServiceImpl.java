package sejong.foodsns.service.member.login.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.login.MemberLogoutDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage;
import sejong.foodsns.service.member.login.MemberLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberLoginServiceImpl implements MemberLoginService {

    private final MemberRepository memberRepository;

    /**
     * Login Service
     * @param memberLoginDto MemberLoginDTO
     * @return 성공 (회원 정보 응답)OK, 실패 NOT_FOUND
     */
    @Override
    public ResponseEntity<MemberResponseDto> login(MemberLoginDto memberLoginDto) {

        Optional<Member> member = of(memberRepository.findMemberByEmail(memberLoginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));

        return new ResponseEntity<>(getMemberResponseDto(member), OK);
    }

    @Override
    public ResponseEntity<MemberLogoutDto> logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        if(session != null) {
            session.invalidate();
            request.getSession(true);
        }

        return new ResponseEntity<>(new MemberLogoutDto(LOGOUT_SUCCESS), OK);
    }


    /**
     * get MemberResponse DTO
     * @param member Optional
     * @return MemberResponseDTO
     */
    private MemberResponseDto getMemberResponseDto(Optional<Member> member) {
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .member(member.get())
                .build();
        return memberResponseDto;
    }
}
