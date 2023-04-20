package sejong.foodsns.log.interceptor.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sejong.foodsns.jwt.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtServiceInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    /**
     * Jwt 토큰 인터셉터 (유효성 검사)
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return 유효하지 않은 토큰 (토큰이 존재하지 않음) false, 토큰이 존재함 true
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("[Http Request Token] = {}", request.getHeader("Authorization"));

        String accessToken = request.getHeader("Authorization");

        if(accessToken != null) {
            String token = JwtProvider.getFormatToken(accessToken);
            return jwtProvider.isValidTokenCheck(token);
        }

        response.sendRedirect("/");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
