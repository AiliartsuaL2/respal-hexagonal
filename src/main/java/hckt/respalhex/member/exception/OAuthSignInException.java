package hckt.respalhex.member.exception;

import hckt.respalhex.member.domain.OAuthInfo;
import lombok.Getter;

import java.net.URI;

@Getter
public class OAuthSignInException extends RuntimeException {
    private static final String REDIRECT_URL = "";
    private final String uid;
    private final String client;

    public OAuthSignInException(OAuthInfo oAuthInfo, String client) {
        super(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage());
        this.uid = oAuthInfo.getUid();
        this.client = client;
    }

    // todo Redirect uri 설정
    public URI getRedirectUri() {
        return URI.create(String.join("/", REDIRECT_URL, client, uid));
    }
}
