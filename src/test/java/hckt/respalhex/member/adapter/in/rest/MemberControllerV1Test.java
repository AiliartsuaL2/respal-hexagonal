package hckt.respalhex.member.adapter.in.rest;

import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.respalhex.auth.adapter.in.handler.JwtExceptionFilter;
import hckt.respalhex.auth.adapter.out.handler.JwtAccessDeniedHandler;
import hckt.respalhex.auth.adapter.out.handler.JwtAuthenticationEntryPoint;
import hckt.respalhex.global.config.SecurityConfig;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
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

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = MemberControllerV1.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class MemberControllerV1Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostMemberUseCase postMemberUseCase;
    @MockBean
    private GetMemberUseCase getMemberUseCase;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("회원 가입 테스트")
    class PostMemberTest {
        private static final String EMAIL = "email";
        private static final String NICKNAME = "nickname";
        private static final String PASSWORD = "password";
        private static final String PICTURE = "picture";
        private static final String PROVIDER = "common";

        @Test
        @DisplayName("정상 케이스")
        void test1() throws Exception {
            //given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER)
                    .build();

            //when
            mockMvc.perform(post("/v1.0.0/member")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .with(csrf()))
                    .andExpect(status().isCreated());

            //then
            then(postMemberUseCase)
                    .should()
                    .create(requestDto);
        }
    }
}
