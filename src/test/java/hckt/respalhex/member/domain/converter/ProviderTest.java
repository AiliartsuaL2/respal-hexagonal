package hckt.respalhex.member.domain.converter;

import static org.assertj.core.api.Assertions.*;

import hckt.respalhex.member.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProviderTest {
    private static final String NOT_EXIST_VALUE = "notExist";
    private static final String COMMON_VALUE = "common";
    private static final String KAKAO_VALUE = "kakao";
    private static final String GOOGLE_VALUE = "google";
    private static final String GITHUB_VALUE = "github";

    @Nested
    @DisplayName("value 값으로 Provider 조회 메서드")
    class FindByValue {
        @Test
        @DisplayName("일반 회원으로 조회시 COMMON 반환")
        void test1() {
            //given
            Provider provider = Provider.COMMON;

            //when
            Provider actual = Provider.findByValue(COMMON_VALUE);

            //then
            assertThat(actual).isEqualTo(provider);
        }

        @Test
        @DisplayName("카카오 회원으로 조회시 KAKAO 반환")
        void test2() {
            //given
            Provider provider = Provider.KAKAO;

            //when
            Provider actual = Provider.findByValue(KAKAO_VALUE);

            //then
            assertThat(actual).isEqualTo(provider);
        }

        @Test
        @DisplayName("구글 회원으로 조회시 GOOGLE 반환")
        void test3() {
            //given
            Provider provider = Provider.GOOGLE;

            //when
            Provider actual = Provider.findByValue(GOOGLE_VALUE);

            //then
            assertThat(actual).isEqualTo(provider);
        }

        @Test
        @DisplayName("깃헙 회원으로 조회시 GITHUB 반환")
        void test4() {
            //given
            Provider provider = Provider.GITHUB;

            //when
            Provider actual = Provider.findByValue(GITHUB_VALUE);

            //then
            assertThat(actual).isEqualTo(provider);
        }

        @Test
        @DisplayName("존재하지 않는 회원으로 조회시 예외 반환")
        void test5() {
            //given & when & then
            assertThatThrownBy(() -> Provider.findByValue(NOT_EXIST_VALUE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION.getMessage());
        }
    }

    @Nested
    @DisplayName("일반 회원 확인 메서드")
    class IsCommon {
        @Test
        @DisplayName("일반 Provider인 경우 true 반환")
        void test1() {
            //given
            Provider common = Provider.findByValue(COMMON_VALUE);

            //when
            boolean actual = common.isCommon();

            //then
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("카카오, 구글, 깃허브 Provider인 경우 false 반환")
        void test2() {
            //given
            Provider kakao = Provider.findByValue(KAKAO_VALUE);
            Provider google = Provider.findByValue(GOOGLE_VALUE);
            Provider github = Provider.findByValue(GITHUB_VALUE);

            //when
            boolean actualKakao = kakao.isCommon();
            boolean actualGoogle = google.isCommon();
            boolean actualGithub = github.isCommon();

            //then
            assertThat(actualKakao).isFalse();
            assertThat(actualGoogle).isFalse();
            assertThat(actualGithub).isFalse();
        }
    }
}
