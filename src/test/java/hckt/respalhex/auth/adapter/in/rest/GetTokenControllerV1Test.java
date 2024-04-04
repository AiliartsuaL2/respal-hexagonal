package hckt.respalhex.auth.adapter.in.rest;

import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.auth.application.port.in.CreateTokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = GetTokenControllerV1.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class GetTokenControllerV1Test {

    //todo MagicString 제거 및 테스트 코드 체크
    private static final String GET_TOKEN_ENDPOINT = "/api/v1.0/token";

    @Autowired
    private MockMvc mockMvc;
    @Value("${respal.web.url}")
    private String webUrl;

    @MockBean
    CreateTokenUseCase createTokenUseCase;

    @Test
    @DisplayName("웹 로그인시 쿼리 파라미터로 토큰을 포함하여 weburl/main 으로 Redirect 된다.")
    void test1() throws Exception {
        // given
        long memberId = 1L;
        String client = "web-dev";
        String token = "token";
        String expectedRedirectUrl = webUrl+"/main?token"+token;

        // when & then
        mockMvc.perform(get(GET_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberId", String.valueOf(memberId))
                        .param("client", client)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUrl));
    }

    @Test
    @DisplayName("앱 로그인시 쿼리 파라미터로 토큰을 포함하여 appCustomScheme/callback 으로 Redirect 된다.")
    void test2() throws Exception {
        // given
        long memberId = 1L;
        String client = "app";
        String token = "token";
        String expectedRedirectUrl = "app://callback?token"+token;

        // when & then
        mockMvc.perform(get(GET_TOKEN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberId", String.valueOf(memberId))
                        .param("client", client)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUrl));
    }

}