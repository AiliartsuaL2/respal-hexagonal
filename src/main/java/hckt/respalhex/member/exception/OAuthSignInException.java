package hckt.respalhex.member.exception;

import hckt.respalhex.member.domain.OAuthInfo;
import lombok.Getter;


@Getter
public class OAuthSignInException extends RuntimeException {
    private final String uid;
    private final String client;

    public OAuthSignInException(OAuthInfo oAuthInfo, String client) {
        super(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage());
        this.uid = oAuthInfo.getUid();
        this.client = client;
    }

    public String getUidParameter() {
            return "?uid=" + this.uid;
    }
}
