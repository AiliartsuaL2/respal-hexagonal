package hckt.respalhex.member.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.member.adapter.dto.request.SignInAdapterRequestDto;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.dto.response.GetTokenResponseDto;
import hckt.respalhex.member.application.port.in.GetTokenUseCase;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import hckt.respalhex.member.exception.OAuthSignInException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SignInControllerV1.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
@ActiveProfiles(value = "test")
class SignInControllerV1Test {
    private static final String COMMON_SIGNIN_ENDPOINT = "/api/v1.0/signin";
    private static final String OAUTH_WEB_ENDPOINT = "/api/v1.0/web/oauth/google";
    private static final String OAUTH_APP_ENDPOINT = "/api/v1.0/app/oauth/google";
    private static final Long MEMBER_ID = 0L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CLIENT_WEB = "web-dev";
    private static final String CLIENT_APP = "app";
    private static final String PROVIDER = "google";
    private static final String CODE = "code";


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SignInUseCase signInUseCase;
    @MockBean
    GetTokenUseCase getTokenUseCase;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${respal.web.url}")
    String webUrl;
    @Value("${respal.app.custom-scheme}")
    String appCustomScheme;
    @Value("${respal.web.path.login}")
    String webLoginPath;
    @Value("${respal.app.path.login}")
    String appLoginPath;
    @Value("${respal.web.path.sign-up}")
    String webSignUpPath;
    @Value("${respal.app.path.sign-up}")
    String appSignUpPath;

    @Nested
    @DisplayName("일반 로그인 테스트")
    class SignIn {
        @Test
        @DisplayName("web 정상 로그인시 /main 으로 Redirect 된다.")
        void test1() throws Exception {
            //given
            SignInAdapterRequestDto requestDto = new SignInAdapterRequestDto(EMAIL, PASSWORD, CLIENT_WEB);
            GetTokenResponseDto token = mock(GetTokenResponseDto.class);
            String expectedRedirectUrl = webUrl + webLoginPath;

            when(token.convertToQueryParam())
                    .thenReturn("");
            when(signInUseCase.signIn(requestDto.convertToApplicationDto()))
                    .thenReturn(MEMBER_ID);
            when(getTokenUseCase.getToken(MEMBER_ID))
                    .thenReturn(token);

            //when & then
            mockMvc.perform(post(COMMON_SIGNIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl(expectedRedirectUrl));
        }

        @Test
        @DisplayName("app 정상 로그인시 /callback 으로 Redirect 된다.")
        void test2() throws Exception {
            //given
            SignInAdapterRequestDto requestDto = new SignInAdapterRequestDto(EMAIL, PASSWORD, CLIENT_APP);
            GetTokenResponseDto token = mock(GetTokenResponseDto.class);
            String expectedRedirectUrl = appCustomScheme + appLoginPath;

            when(token.convertToQueryParam())
                    .thenReturn("");
            when(signInUseCase.signIn(requestDto.convertToApplicationDto()))
                    .thenReturn(MEMBER_ID);
            when(getTokenUseCase.getToken(MEMBER_ID))
                    .thenReturn(token);

            //when & then
            mockMvc.perform(post(COMMON_SIGNIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(expectedRedirectUrl));
        }

        @Test
        @DisplayName("로그인 실패시 400 예외가 발생한다.")
        void test3() throws Exception {
            //given
            SignInAdapterRequestDto requestDto = new SignInAdapterRequestDto(EMAIL, PASSWORD, CLIENT_WEB);

            when(signInUseCase.signIn(requestDto.convertToApplicationDto()))
                    .thenThrow(IllegalArgumentException.class);

            //when & then
            mockMvc.perform(post(COMMON_SIGNIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayName("소셜 로그인 테스트")
    class OAuthSignIn {
        @Test
        @DisplayName("web 정상 로그인시 /main 으로 Redirect 된다.")
        void test1() throws Exception {
            //given
            OAuthSignInRequestDto requestDto = new OAuthSignInRequestDto(CLIENT_WEB, PROVIDER, CODE);
            GetTokenResponseDto token = mock(GetTokenResponseDto.class);
            String expectedRedirectUrl = webUrl + webLoginPath;

            when(token.convertToQueryParam())
                    .thenReturn("");
            when(signInUseCase.signIn(requestDto))
                    .thenReturn(MEMBER_ID);
            when(getTokenUseCase.getToken(MEMBER_ID))
                    .thenReturn(token);

            //when & then
            mockMvc.perform(get(OAUTH_WEB_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("code", CODE)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(expectedRedirectUrl));
        }

        @Test
        @DisplayName("app 정상 로그인시 /callback 으로 Redirect 된다.")
        void test2() throws Exception {
            //given
            OAuthSignInRequestDto requestDto = new OAuthSignInRequestDto(CLIENT_APP, PROVIDER, CODE);
            GetTokenResponseDto token = mock(GetTokenResponseDto.class);
            String expectedRedirectUrl = appCustomScheme + appLoginPath;

            when(token.convertToQueryParam())
                    .thenReturn("");
            when(signInUseCase.signIn(requestDto))
                    .thenReturn(MEMBER_ID);
            when(getTokenUseCase.getToken(MEMBER_ID))
                    .thenReturn(token);

            //when & then
            mockMvc.perform(get(OAUTH_APP_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("code", CODE)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(expectedRedirectUrl));
        }

        @Test
        @DisplayName("web 로그인 실패시 /signup/social 으로 Redirect 된다.")
        void test3() throws Exception {
            //given
            String uid = "?uid=uid";
            String expectedRedirectUrl = webUrl + webSignUpPath + uid;
            OAuthSignInException exception = mock(OAuthSignInException.class);
            when(exception.getUidParameter())
                    .thenReturn(uid);

            when(signInUseCase.signIn(any(OAuthSignInRequestDto.class)))
                    .thenThrow(exception);

            //when & then
            mockMvc.perform(get(OAUTH_WEB_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("code", CODE)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(expectedRedirectUrl));
        }

        @Test
        @DisplayName("app 로그인 실패시 /signup 으로 Redirect 된다.")
        void test4() throws Exception {
            //given
            String uid = "?uid=uid";
            String expectedRedirectUrl = appCustomScheme + appSignUpPath + uid;
            OAuthSignInException exception = mock(OAuthSignInException.class);
            when(exception.getUidParameter())
                    .thenReturn(uid);

            when(signInUseCase.signIn(any(OAuthSignInRequestDto.class)))
                    .thenThrow(exception);

            //when & then
            mockMvc.perform(get(OAUTH_APP_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("code", CODE)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(expectedRedirectUrl));
        }
    }
}
