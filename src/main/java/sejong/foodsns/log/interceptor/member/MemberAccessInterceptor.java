package sejong.foodsns.log.interceptor.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.jwt.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static sejong.foodsns.domain.member.MemberType.*;

@Slf4j
@RequiredArgsConstructor
public class MemberAccessInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    /**
     * 관리자 페이지와 일반 유저를 나누기 위한 인터셉터
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return 회원이 관리자이면 true, 일반유저이면 false
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("[Http Request] = {}", request);

        String token = request.getHeader("X-AUTH-TOKEN");
        MemberResponseDto loginDto = jwtProvider.getLoginDto(token);
        MemberType memberType = loginDto.getMemberType();

        return memberType == ADMIN;
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
