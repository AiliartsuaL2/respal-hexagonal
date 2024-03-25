package hckt.respalhex.member.application.port.in;

public interface SignInUseCase {
    Long signIn(String email, String password);
    Long signIn(String client, String provider, String code);
}
