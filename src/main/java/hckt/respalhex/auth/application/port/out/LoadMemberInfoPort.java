package hckt.respalhex.auth.application.port.out;

import java.util.concurrent.TimeoutException;

public interface LoadMemberInfoPort {
    Long signIn(String email, String password) throws TimeoutException;
}
