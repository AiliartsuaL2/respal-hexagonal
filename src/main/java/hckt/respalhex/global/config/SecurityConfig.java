package hckt.respalhex.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.auth.application.port.in.JwtAuthenticationFilter;
import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] OAUTH_V1_WHITELIST = {
            "/api/v1.0/web-dev/oauth/google",
            "/api/v1.0/web-dev/oauth/kakao",
            "/api/v1.0/web-dev/oauth/github",
            "/api/v1.0/app/oauth/google",
            "/api/v1.0/app/oauth/kakao",
            "/api/v1.0/app/oauth/github"
    };

    private final GetTokenInfoProvider getTokenInfoProvider;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/resources/templates/member/login.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/member/login").permitAll()
                        .requestMatchers("/api/v1.0/sign-up").permitAll()
                        .requestMatchers("/api/v1.0/sign-in").permitAll()
                        // 0410 token 로직 변경 ( redirect -> sqs 활용 내부 통신)
//                        .requestMatchers("/api/v1.0/token").permitAll()
                        .requestMatchers(OAUTH_V1_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthenticationFilter(getTokenInfoProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
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
