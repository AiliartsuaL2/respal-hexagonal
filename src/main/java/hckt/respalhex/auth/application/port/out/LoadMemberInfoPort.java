package hckt.respalhex.auth.application.port.out;

public interface LoadMemberInfoPort {
    Long signIn(String email, String password);
}
