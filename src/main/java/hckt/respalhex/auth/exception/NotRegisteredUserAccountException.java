package hckt.respalhex.auth.exception;

import io.jsonwebtoken.JwtException;

public class NotRegisteredUserAccountException extends JwtException {
    public NotRegisteredUserAccountException(String message) {
        super(message);
    }
}
