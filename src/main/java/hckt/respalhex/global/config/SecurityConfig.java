package hckt.respalhex.global.config;

import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.auth.application.port.in.JwtAuthenticationFilter;
import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final GetTokenInfoProvider getTokenInfoProvider;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/v1.0.0/member").permitAll()
                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                // JwtAuthenticationFilter를 UserIdPasswordAuthenticationFilter 전에 넣는다 + 토큰에 저장된 유저정보를 활용하여야 하기 때문에 CustomUserDetailService 클래스를 생성
                .addFilterBefore(new JwtAuthenticationFilter(getTokenInfoProvider), // 필터를 등록함, 파라미터 - 1번째 : 커스텀한 필터링, 2번쨰 : 필터링전 커스텀 필터링 수행
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter,JwtAuthenticationFilter.class) // jwt 에러처리를 위한 필터등록
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }
}
