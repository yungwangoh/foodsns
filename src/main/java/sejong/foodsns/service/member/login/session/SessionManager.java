package sejong.foodsns.service.member.login.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * @param value
     * @param response
     */
    public void createSession(Object value, HttpServletResponse response) {

        // session id create and value save session
        String sessionId= UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // cookie create
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     * @param request
     * @return
     */
    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionStore == null) {
            return null;
        }
        return sessionStore.get(cookie.getValue());
    }

    /**
     * 세션 찾기
     * @param request
     * @param cookieName
     * @return
     */
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    /**
     * 세션 만료
     * @param request
     */
    public void expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie != null) {
            sessionStore.remove(cookie.getValue());
        }
    }
}
