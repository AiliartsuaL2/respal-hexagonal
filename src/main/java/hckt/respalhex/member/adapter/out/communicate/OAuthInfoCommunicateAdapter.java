package hckt.respalhex.member.adapter.out.communicate;

import hckt.respalhex.global.annotation.CommunicateAdapter;
import hckt.respalhex.member.adapter.out.communicate.dto.OAuthCommunicateResponseDto;
import hckt.respalhex.member.adapter.out.communicate.dto.OAuthToken;
import hckt.respalhex.member.config.OAuth2ProviderProperties;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.config.ProviderInfo;
import hckt.respalhex.member.domain.OAuthInfo;
import hckt.respalhex.member.domain.converter.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@CommunicateAdapter
@RequiredArgsConstructor
public class OAuthInfoCommunicateAdapter {
    @Value("respal.url")
    private String url;
    private final OAuth2ProviderProperties oAuth2ProviderProperties;

    public OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto) {
        Provider provider = Provider.findByValue(requestDto.provider());
        String oAuthRedirectUri = requestDto.getOAuthRedirectUri(url, "v1.0");
        ProviderInfo providerInfo = oAuth2ProviderProperties.get(provider);
        OAuthCommunicateResponseDto responseDto = communicateWithOAuthServer(oAuthRedirectUri, providerInfo, requestDto.code());
        return new OAuthInfo(provider, responseDto.getEmail(), responseDto.getImage(), responseDto.getNickname());
    }

    private OAuthCommunicateResponseDto communicateWithOAuthServer(String redirectUri, ProviderInfo providerInfo, String code) {
        String oAuthAccessToken = getOAuthAccessToken(redirectUri, providerInfo, code);
        return getUserInfo(providerInfo, oAuthAccessToken);
    }

    private String getOAuthAccessToken(String redirectUri, ProviderInfo providerInfo, String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl(providerInfo.getTokenUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        OAuthToken oAuthToken = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", providerInfo.getGrantType())
                        .queryParam("client_id", providerInfo.getClientId())
                        .queryParam("client_secret", providerInfo.getClientSecret())
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(OAuthToken.class)
                .block();
        return oAuthToken.getAccessToken();
    }

    private OAuthCommunicateResponseDto getUserInfo(ProviderInfo providerInfo, String accessToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl(providerInfo.getInfoUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                .build();
        return webClient.get()
                .retrieve()
                .bodyToMono(providerInfo.getResponseDto().getClass())
                .block();
    }
}
