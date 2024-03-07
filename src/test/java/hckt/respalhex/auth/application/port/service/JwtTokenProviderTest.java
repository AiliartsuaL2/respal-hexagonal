package hckt.respalhex.auth.application.port.service;

import hckt.respalhex.auth.exception.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Transactional
class JwtTokenProviderTest {

    private static final String SECRET_KEY = "secretKey";
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
    UserDetailsService userDetailServiceMock = mock(UserDetailServiceImpl.class);
    JwtTokenProvider jwtTokenProviderMock = new JwtTokenProvider(SECRET_KEY, ACCESS_TOKEN_VALIDITY_SECONDS, REFRESH_TOKEN_VALIDITY_SECONDS, userDetailServiceMock);

    @Nested
    @DisplayName("액세스 토큰 생성 테스트")
    class CreateAccessToken {
        @Test
        @DisplayName("토큰 생성시 .으로 나뉜 세개의 인자가 있다. (header.payload.signature)")
        void test1() {
            // given
            String payload = "1";

            // when
            String accessToken = jwtTokenProviderMock.createAccessToken(payload);

            // then
            assertThat(accessToken.split("\\.")).hasSize(3);
        }

        @Test
        @DisplayName("payload가 null이면 예외가 발생한다.")
        void test2() {
            // given
            String payload = null;

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.createAccessToken(payload))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PAYLOAD_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 생성 테스트")
    class CreateRefreshToken {
        @Test
        @DisplayName("토큰 생성시 .으로 나뉜 세개의 인자가 있다. (header.payload.signature)")
        void test1() {
            // given
            String payload = "1";

            // when
            String accessToken = jwtTokenProviderMock.createRefreshToken(payload);

            // then
            assertThat(accessToken.split("\\.")).hasSize(3);
        }

        @Test
        @DisplayName("payload가 null이면 예외가 발생한다.")
        void test2() {
            // given
            String payload = null;

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.createRefreshToken(payload))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PAYLOAD_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("payload 추출 테스트")
    class GetPayLoad {
        @Test
        @DisplayName("생성 토큰의 payload와 추출한 payload의 값이 일치한다.")
        void test1() {
            // given
            String payload = "1";
            String accessToken = jwtTokenProviderMock.createAccessToken(payload);

            // when
            String result = jwtTokenProviderMock.getPayload(accessToken);

            // then
            assertThat(result).isEqualTo(payload);
        }

        @Test
        @DisplayName("입력 토큰이 null인 경우 예외가 발생한다.")
        void test2() {
            // given
            String accessToken = null;

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.getPayload(accessToken))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("입력 토큰의 형식이 올바르지 않은 경우 예외가 발생한다.")
        void test3() {
            // given
            String accessToken = "invalidToken";

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.getPayload(accessToken))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.INVALID_TOKEN_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("payload 추출 테스트")
    class IsValid {
        @Test
        @DisplayName("토큰이 정상적인 경우 true를 반환한다.")
        void test1() {
            // given
            String payload = "1";
            String accessToken = jwtTokenProviderMock.createAccessToken(payload);

            // when
            boolean isValid = jwtTokenProviderMock.isValid(accessToken);

            // then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("토큰이 존재하지 않는 경우 false를 발생한다.")
        void test2() {
            // given
            String accessToken = null;

            // when
            boolean isValid = jwtTokenProviderMock.isValid(accessToken);

            // then
            assertThat(isValid).isFalse();
        }


        @Test
        @DisplayName("토큰의 Secret Key가 다른 경우 false를 반환한다.")
        void test3() {
            // given
            JwtTokenProvider otherJwtTokenProviderMock = new JwtTokenProvider("invalidSecretKey", ACCESS_TOKEN_VALIDITY_SECONDS, REFRESH_TOKEN_VALIDITY_SECONDS, userDetailServiceMock);
            String payload = "1";
            String accessToken = otherJwtTokenProviderMock.createAccessToken(payload);

            // when
            boolean isValid = jwtTokenProviderMock.isValid(accessToken);

            // then
            assertThat(isValid).isFalse();
        }


        @Test
        @DisplayName("토큰의 유효기간이 만료한 경우 false를 반환한다.")
        void test4() {
            // given
            JwtTokenProvider otherJwtTokenProviderMock = new JwtTokenProvider(SECRET_KEY, 0, 0, userDetailServiceMock);
            String payload = "1";
            String accessToken = otherJwtTokenProviderMock.createAccessToken(payload);

            // when
            boolean isValid = jwtTokenProviderMock.isValid(accessToken);

            // then
            assertThat(isValid).isFalse();
        }
    }

    @Nested
    @DisplayName("검증 및 예외처리 테스트")
    class ValidateAndThrow {
        @Test
        @DisplayName("정상 토큰인 경우 아무것도 반환하지 않는다.")
        void test1() {
            // given
            String payload = "1";
            String accessToken = jwtTokenProviderMock.createAccessToken(payload);

            // when & then
            jwtTokenProviderMock.validateAndThrow(accessToken);
        }

        @Test
        @DisplayName("토큰이 존재하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            String accessToken = null;

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.validateAndThrow(accessToken))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN_EXCEPTION.getMessage());
        }


        @Test
        @DisplayName("토큰의 Secret Key가 다른 경우 예외를 발생시킨다.")
        void test3() {
            // given
            JwtTokenProvider otherJwtTokenProviderMock = new JwtTokenProvider("invalidSecretKey", ACCESS_TOKEN_VALIDITY_SECONDS, REFRESH_TOKEN_VALIDITY_SECONDS, userDetailServiceMock);
            String payload = "1";
            String accessToken = otherJwtTokenProviderMock.createAccessToken(payload);

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.validateAndThrow(accessToken))
                    .isInstanceOf(SignatureException.class);
        }


        @Test
        @DisplayName("토큰의 유효기간이 만료한 경우 false를 반환한다.")
        void test4() {
            // given
            JwtTokenProvider otherJwtTokenProviderMock = new JwtTokenProvider(SECRET_KEY, 0, 0, userDetailServiceMock);
            String payload = "1";
            String accessToken = otherJwtTokenProviderMock.createAccessToken(payload);

            // when & then
            assertThatThrownBy(() -> jwtTokenProviderMock.validateAndThrow(accessToken))
                    .isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Nested
    @DisplayName("Authentication")
    class GetAuthentication {

    }

    @Nested
    @DisplayName("")
    class ResolveToken {

    }
}