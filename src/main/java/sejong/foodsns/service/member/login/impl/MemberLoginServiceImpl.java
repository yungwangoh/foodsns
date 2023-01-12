package sejong.foodsns.service.member.login.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttribute;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.login.MemberLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage.*;
import static sejong.foodsns.service.member.login.session.SessionConst.LOGIN_MEMBER;

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
    public ResponseEntity<String> login(MemberLoginDto memberLoginDto, HttpServletRequest request) {

        Optional<Member> member = memberRepository.findByEmail(memberLoginDto.getEmail());
        if(member.isEmpty()) {
            return new ResponseEntity<>(LOGIN_FAIL, NOT_FOUND);
        }

        sessionCreate(request, member);
        return new ResponseEntity<>(LOGIN_SUCCESS, OK);
    }

    /**
     * Logout Service
     * @param request HttpServletRequest
     * @return 성공 : 로그아웃에 성공하셨습니다.
     */
    @Override
    public ResponseEntity<String> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return new ResponseEntity<>(LOGOUT_SUCCESS, OK);
        } else {
            return new ResponseEntity<>(LOGOUT_FAIL, NOT_FOUND);
        }
    }

    /**
     * 세션이 있을 때 로그인 유지.
     * @param member Member Login Information
     * @return 성공 : OK, 실패 : NOT_FOUND
     */
    @Override
    public ResponseEntity<String> keepSessionLogin(@SessionAttribute(name = LOGIN_MEMBER, required = false) Member member) {
        if(member == null) {
            return new ResponseEntity<>(LOGIN_FAIL, NOT_FOUND);
        }
        return new ResponseEntity<>(LOGIN_SUCCESS, OK);
    }

    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    private void sessionCreate(HttpServletRequest request, Optional<Member> member) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, getMember(member));
    }
}
