package hckt.respalhex.auth.adapter.dto.request;

public record OAuthSignInRequestDto(String client, String provider, String code) {
}
