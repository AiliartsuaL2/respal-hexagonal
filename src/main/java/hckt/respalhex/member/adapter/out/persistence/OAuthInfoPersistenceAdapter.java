package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.OAuthInfo;

import java.util.Optional;

public interface OAuthInfoPersistenceAdapter {
    Optional<OAuthInfo> loadOAuthInfo(String uid);
}
