package hckt.respalhex.member.config;


import hckt.respalhex.member.adapter.out.communicate.dto.OAuthCommunicateResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProviderInfo {
    private OAuthCommunicateResponseDto responseDto;
    private final String grantType = "authorization_code";
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl ;
    private final String infoUrl ;
    private final String logoutUrl ;
}
