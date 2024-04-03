package hckt.respalhex.member.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.member.adapter.dto.request.SignInAdapterRequestDto;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SignInControllerV1.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class SignInControllerV1Test {
    private static final String COMMON_SIGNIN_ENDPOINT = "/api/v1.0/signin";
    private static final String OAUTH_CALLBACK_ENDPOINT = "/api/v1.0/web/oauth/google";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PICTURE = "picture";
    private static final String PROVIDER = "common";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SignInUseCase signInUseCase;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("일반 로그인 테스트")
    class SignIn {
        @Test
        @DisplayName("정상 로그인시 Redirect가 된다.")
        void test1() throws Exception {
            //given
            SignInAdapterRequestDto requestDto = new SignInAdapterRequestDto(EMAIL, PASSWORD);

            //when
            mockMvc.perform(post(COMMON_SIGNIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection());

            //then
            then(signInUseCase)
                    .should()
                    .signIn(requestDto.convertToApplicationDto());
        }
    }

    @Nested
    @DisplayName("소셜 로그인 테스트")
    class OAuthSignIn {
        @Test
        @DisplayName("정상 로그인시 Redirect가 된다.")
        void test1() throws Exception {
            //given & when
            mockMvc.perform(get(OAUTH_CALLBACK_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("code", "code")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection());

            //then
            then(signInUseCase)
                    .should()
                    .signIn(any(OAuthSignInRequestDto.class));
        }
    }
}