package hckt.respalhex.member.adapter.out;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import hckt.respalhex.member.adapter.out.communicate.OAuthInfoCommunicateAdapter;
import hckt.respalhex.member.adapter.out.persistence.OAuthInfoPersistenceAdapter;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoadOAuthInfoAdapterTest {
    OAuthInfoCommunicateAdapter oAuthInfoCommunicateAdapter = mock(OAuthInfoCommunicateAdapter.class);
    OAuthInfoPersistenceAdapter oAuthInfoPersistenceAdapter = mock(OAuthInfoPersistenceAdapter.class);
    LoadOAuthInfoAdapter loadOAuthInfoAdapter = new LoadOAuthInfoAdapter(oAuthInfoCommunicateAdapter,
            oAuthInfoPersistenceAdapter);

    @Test
    @DisplayName("uid로 OAuth 정보 조회시 DB에서 조회해온다.")
    void test1() {
        //given
        String uid = "uid";

        //when
        loadOAuthInfoAdapter.loadOAuthInfo(uid);

        //then
        then(oAuthInfoCommunicateAdapter).should(times(0)).loadOAuthInfo(any());
        then(oAuthInfoPersistenceAdapter).should(times(1)).loadOAuthInfo(any());
    }

    @Test
    @DisplayName("OAuth signin dto로 OAuth 정보 조회시 OAuth 서버에서 조회해온다.")
    void test2() {
        //given
        OAuthSignInRequestDto requestDto = mock(OAuthSignInRequestDto.class);

        //when
        loadOAuthInfoAdapter.loadOAuthInfo(requestDto);

        //then
        then(oAuthInfoPersistenceAdapter).should(times(0)).loadOAuthInfo(any());
        then(oAuthInfoCommunicateAdapter).should(times(1)).loadOAuthInfo(any());
    }
}
