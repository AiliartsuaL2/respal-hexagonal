package hckt.respalhex.auth.application.port.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;
import hckt.respalhex.auth.application.port.out.CommandRefreshTokenPort;
import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;
import hckt.respalhex.auth.application.port.out.LoadRefreshTokenPort;
import hckt.respalhex.auth.application.port.service.provider.CreateTokenProvider;
import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import hckt.respalhex.auth.domain.Token;
import hckt.respalhex.auth.exception.ErrorMessage;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import static org.mockito.Mockito.*;

@Transactional
class JwtServiceTest {
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("secretKey", 360000, 1580000, mock(UserDetailServiceImpl.class));
    CommandRefreshTokenPort commandRefreshTokenPortMock = mock(CommandRefreshTokenPort.class);
    LoadRefreshTokenPort loadRefreshTokenPortMock = mock(LoadRefreshTokenPort.class);
    LoadMemberInfoPort loadMemberInfoPortMock = mock(LoadMemberInfoPort.class);
    CreateTokenProvider createTokenProviderMock = jwtTokenProvider;
    GetTokenInfoProvider getTokenInfoProviderMock = jwtTokenProvider;
    JwtService jwtService = new JwtService(commandRefreshTokenPortMock, loadRefreshTokenPortMock, createTokenProviderMock, getTokenInfoProviderMock, loadMemberInfoPortMock);

    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "password";
    private static final Long MEMBER_ID = 0L;
    private static final Long MEMBER_ID_NULL = null;
    private static final String TOKEN_NULL = null;
    private static final String INVALID_TOKEN =  new JwtTokenProvider("invalid", 10000, 1000000, mock(UserDetailServiceImpl.class)).createAccessToken("1");

    @Nested
    @DisplayName("토큰 생성 테스트")
    class CreateToken {
        @Test
        @DisplayName("토큰 생성시 header, payload, signature이 .으로 나눠진다")
        void test1() {
            //given & when
            Token token = jwtService.create(MEMBER_ID);

            //then
            assertThat(token.accessToken().split("\\.").length).isEqualTo(3);
            assertThat(token.refreshToken().split("\\.").length).isEqualTo(3);
        }

        @Test
        @DisplayName("memberId가 null인 경우 예외 발생")
        void test2() {
            //given & when & then
            assertThatThrownBy(() -> jwtService.create(MEMBER_ID_NULL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("payload 추출 테스트")
    class ExtractPayload {
        @Test
        @DisplayName("정상 토큰 추출시 회원 ID 반환")
        void test1() {
            //given
            Token token = jwtService.create(MEMBER_ID);

            //when
            Long payload = jwtService.extractPayload(token.accessToken());

            //then
            assertThat(payload).isEqualTo(MEMBER_ID);
        }

        @Test
        @DisplayName("토큰이 null인 경우 예외 발생")
        void test2() {
            //given & when & then
            assertThatThrownBy(() -> jwtService.extractPayload(TOKEN_NULL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ACCESS_TOKEN_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("유효하지 않는 토큰(SecretKey가 다름)인 경우 예외 발생")
        void test3() {
            //given & when & then
            assertThatThrownBy(() -> jwtService.extractPayload(INVALID_TOKEN))
                    .isInstanceOf(SignatureException.class);
        }

        @Test
        @DisplayName("만료일이 지난 토큰인 경우 예외 발생")
        void test4() {
            //given
            CreateTokenProvider createTokenProvider = new JwtTokenProvider("secretKey", 0, 0, mock(UserDetailServiceImpl.class));
            JwtService jwtServiceMock = new JwtService(commandRefreshTokenPortMock, loadRefreshTokenPortMock,
                    createTokenProvider, getTokenInfoProviderMock, loadMemberInfoPortMock);
            Token expiredToken = jwtServiceMock.create(MEMBER_ID);

            //when & then
            assertThatThrownBy(() -> jwtService.extractPayload(expiredToken.accessToken()))
                    .isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Nested
    @DisplayName("액세스 토큰 재발급 테스트")
    class RenewAccessToken {
        @Test
        @DisplayName("정상 Refresh 토큰으로 요청시 정상 액세스 토큰 반환")
        void test1() {
            //given
            Token token = jwtService.create(MEMBER_ID);
            when(loadRefreshTokenPortMock.findByKeyId(MEMBER_ID))
                    .thenReturn(Optional.of(token.refreshToken()));

            //when
            String renewedAccessToken = jwtService.renewAccessToken(token.refreshToken());

            //then
            assertThat(jwtService.extractPayload(renewedAccessToken)).isEqualTo(MEMBER_ID);
            assertThat(renewedAccessToken.split("\\.").length).isEqualTo(3);
        }

        @Test
        @DisplayName("리프레시 토큰이 null인 경우 예외 발생")
        void test2() {
            //given & when & then
            assertThatThrownBy(() -> jwtService.renewAccessToken(TOKEN_NULL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("유효하지 않는 토큰(SecretKey가 다름)인 경우 예외 발생")
        void test3() {
            //given & when & then
            assertThatThrownBy(() -> jwtService.extractPayload(INVALID_TOKEN))
                    .isInstanceOf(SignatureException.class);
        }

        @Test
        @DisplayName("만료일이 지난 리프레시 토큰인 경우 예외 발생")
        void test4() {
            //given
            CreateTokenProvider createTokenProvider = new JwtTokenProvider("secretKey", 0, 0, mock(UserDetailServiceImpl.class));
            JwtService jwtServiceMock = new JwtService(commandRefreshTokenPortMock, loadRefreshTokenPortMock,
                    createTokenProvider, getTokenInfoProviderMock, loadMemberInfoPortMock);
            Token expiredToken = jwtServiceMock.create(MEMBER_ID);

            //when & then
            assertThatThrownBy(() -> jwtService.extractPayload(expiredToken.refreshToken()))
                    .isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignInTest {
        @Test
        @DisplayName("정상 로그인시 토큰 발급")
        void test1() {
            // given
            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);
            when(loadMemberInfoPortMock.signIn(logInRequestDto)).thenReturn(MEMBER_ID);

            // when
            LogInResponseDto logInResponseDto = jwtService.signIn(logInRequestDto);

            // then
            assertThat(logInResponseDto.accessToken().split("\\.").length).isEqualTo(3);
            assertThat(logInResponseDto.refreshToken().split("\\.").length).isEqualTo(3);

            Long memberId = jwtService.extractPayload(logInResponseDto.accessToken());
            assertThat(memberId).isEqualTo(MEMBER_ID);
        }

        @Test
        @DisplayName("loadMemberInfoPort.signIn() null 반환 시 IAE 발생")
        void test3() {
            // given
            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);
            when(loadMemberInfoPortMock.signIn(logInRequestDto)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> jwtService.signIn(logInRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.INVALID_MEMBER_EXCEPTION.getMessage());
        }
    }
}
