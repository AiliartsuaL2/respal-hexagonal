package hckt.respalhex.member.application.dto.request;

public record OAuthSignInRequestDto(String client, String provider, String code) {
    // 로그인으로 들어온 RedirectUrl
    // /api/v1.0/{client}/oauth/{provider}
    public String getOAuthRedirectUri(String urlPrefix, String version) {
        return String.join("/", urlPrefix, "api", version, client, "oauth", provider);
    }
}
