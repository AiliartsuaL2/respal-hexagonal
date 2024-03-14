package hckt.respalhex.auth.adapter.out.message;

import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;

public class LoginMemberMessage implements LoadMemberInfoPort {
    @Override
    // 회원 도메인과 통신하여 memberId를 가져온다.
    public Long signIn(String email, String password) {
        return null;
    }
}
