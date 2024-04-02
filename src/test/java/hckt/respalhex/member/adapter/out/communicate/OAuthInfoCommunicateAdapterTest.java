package hckt.respalhex.member.adapter.out.communicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hckt.respalhex.member.config.OAuth2ProviderProperties;
import hckt.respalhex.member.config.ProviderInfo;
import hckt.respalhex.member.domain.converter.Provider;
import hckt.respalhex.member.exception.CommunicationException;
import hckt.respalhex.member.exception.ErrorMessage;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OAuthInfoCommunicateAdapterTest {
    private static final String URL = "url";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String INFO_URL = "infoUrl";
    private static final String LOGOUT_URL = "logoutUrl";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REDIRECT_URI = "redirectUri";

    OAuth2ProviderProperties oAuth2ProviderProperties = mock(OAuth2ProviderProperties.class);
    OAuthInfoCommunicateAdapter oAuthInfoCommunicateAdapter = new OAuthInfoCommunicateAdapter(URL, oAuth2ProviderProperties);

    MockWebServer mockWebServer;
    String mockWebServerUrl ;

    @BeforeEach
    void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServerUrl = mockWebServer.url("").toString();
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("OAuth 서버 액세스 토큰 조회 테스트")
    class GetOAuthAccessToken {
        @Test
        @DisplayName("OAuth Server 400 응답시 예외 발생")
        void test1() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, INFO_URL,
                    LOGOUT_URL);
            when(oAuth2ProviderProperties.get(Provider.GOOGLE)).thenReturn(providerInfo);
            String code = "code";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(400)
                    .setBody("errorMessage"));

            //when & then
            assertThatThrownBy(() -> oAuthInfoCommunicateAdapter.getOAuthAccessToken(REDIRECT_URI, providerInfo, code))
                    .isInstanceOf(CommunicationException.class)
                    .hasMessage(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("OAuth Server 200 응답시, 액세스 토큰 반환")
        void test2() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, INFO_URL,
                    LOGOUT_URL);
            when(oAuth2ProviderProperties.get(Provider.GOOGLE)).thenReturn(providerInfo);

            String responseJson = "{"
                    + "\"access_token\":\"" + ACCESS_TOKEN + "\""
                    + "}";
            String code = "code";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson));

            //when
            String oAuthAccessToken = oAuthInfoCommunicateAdapter.getOAuthAccessToken(REDIRECT_URI, providerInfo, code);

            //then
            assertThat(oAuthAccessToken).isEqualTo(ACCESS_TOKEN);
        }
    }
}
