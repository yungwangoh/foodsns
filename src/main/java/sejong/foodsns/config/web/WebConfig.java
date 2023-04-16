package sejong.foodsns.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sejong.foodsns.jwt.JwtProvider;
import sejong.foodsns.log.interceptor.member.JwtServiceInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new JwtServiceInterceptor(jwtProvider))
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/member/**", "/boards");
    }
}
