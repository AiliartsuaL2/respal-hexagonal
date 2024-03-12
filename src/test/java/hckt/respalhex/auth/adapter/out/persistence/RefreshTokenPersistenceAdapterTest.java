package hckt.respalhex.auth.adapter.out.persistence;

import hckt.respalhex.auth.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RefreshTokenPersistenceAdapterTest {
    @Autowired
    private RefreshTokenPersistenceAdapter refreshTokenPersistenceAdapter;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final Long KEY_ID = 0L;
    private static final String REFRESH_TOKEN = "refreshToken";

    @Nested
    @DisplayName("리프레시 토큰 생성 테스트")
    class Create {
        @Test
        @DisplayName("토큰 생성시 사용했던 key Id로 조회가 가능하다")
        void test1() {
            // given & when
            refreshTokenPersistenceAdapter.create(KEY_ID, REFRESH_TOKEN);

            // then
            Optional<String> refreshTokenByKeyId = refreshTokenRepository.findTokenByKeyId(KEY_ID);
            assertThat(refreshTokenByKeyId).isPresent();
        }

        @Test
        @DisplayName("토큰 생성시 key Id가 존재하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            Long keyIdNull = null;

            // when & then
            assertThatThrownBy(() -> refreshTokenPersistenceAdapter.create(keyIdNull, REFRESH_TOKEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_KEY_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("토큰 생성시 Refresh Token이 존재하지 않는 경우 예외가 발생한다.")
        void test3() {
            // given
            String refreshTokenNull = null;

            // when & then
            assertThatThrownBy(() -> refreshTokenPersistenceAdapter.create(KEY_ID, refreshTokenNull))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 삭제 테스트")
    class Delete {
        @Test
        @DisplayName("토큰 삭제시 해당 키아이디로 조회가 불가능하다")
        void test1() {
            // given
            refreshTokenPersistenceAdapter.create(KEY_ID, REFRESH_TOKEN);

            // when
            refreshTokenPersistenceAdapter.delete(REFRESH_TOKEN);

            // then
            Optional<String> refreshTokenByKeyId = refreshTokenRepository.findTokenByKeyId(KEY_ID);
            assertThat(refreshTokenByKeyId).isEmpty();
        }

        @Test
        @DisplayName("refresh Token이 없는 경우 예외가 발생한다.")
        void test2() {
            // given
            String refreshTokenNull = null;

            // when & then
            assertThatThrownBy(() -> refreshTokenPersistenceAdapter.delete(refreshTokenNull))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 조회 테스트")
    class FindByKeyId {
        @Test
        @DisplayName("이미 존재하는 토큰의 경우 해당 토큰에 해당하는 회원 ID로 조회가 가능하다.")
        void test1() {
            // given
            refreshTokenPersistenceAdapter.create(KEY_ID, REFRESH_TOKEN);

            // when
            Optional<String> refreshTokenByKeyId = refreshTokenRepository.findTokenByKeyId(KEY_ID);

            // then
            assertThat(refreshTokenByKeyId).isPresent();
        }

        @Test
        @DisplayName("refresh Token이 없는 경우 예외가 발생한다.")
        void test2() {
            // given
            Long refreshTokenNull = null;

            // when & then
            assertThatThrownBy(() -> refreshTokenPersistenceAdapter.findByKeyId(refreshTokenNull))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_KEY_ID_EXCEPTION.getMessage());
        }
    }
}