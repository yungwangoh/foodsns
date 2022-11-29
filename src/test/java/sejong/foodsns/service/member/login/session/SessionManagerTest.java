package sejong.foodsns.service.member.login.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import sejong.foodsns.domain.member.Member;

import static org.assertj.core.api.Assertions.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        // 세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        sessionManager.createSession(member, response);

        // 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}