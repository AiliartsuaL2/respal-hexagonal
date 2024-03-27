package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.adapter.out.communicate.OAuthInfoCommunicateAdapter;
import hckt.respalhex.member.adapter.out.persistence.OAuthInfoPersistenceAdapter;

public interface LoadOAuthInfoPort extends OAuthInfoCommunicateAdapter, OAuthInfoPersistenceAdapter {
}
