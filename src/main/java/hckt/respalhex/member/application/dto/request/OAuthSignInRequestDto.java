package hckt.respalhex.member.application.dto.request;

public record OAuthSignInRequestDto(String client, String provider, String code) {
}
