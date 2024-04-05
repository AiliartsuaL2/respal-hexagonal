package hckt.respalhex.auth.adapter.in.rest;

import hckt.respalhex.auth.application.port.in.CreateTokenUseCase;
import hckt.respalhex.auth.domain.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1.0")
@Slf4j
public class GetTokenControllerV1 {
    private final CreateTokenUseCase createTokenUseCase;
    private final String webUrl;
    private final String appCustomScheme;
    private static final String APP_LOGIN_PATH = "callback";
    private static final String WEB_LOGIN_PATH = "/main";

    public GetTokenControllerV1(CreateTokenUseCase createTokenUseCase, @Value("${respal.web.url}") String webUrl, @Value("${respal.app.custom-scheme}") String appCustomScheme) {
        this.createTokenUseCase = createTokenUseCase;
        this.webUrl = webUrl;
        this.appCustomScheme = appCustomScheme;
    }

    /**
     * WEB
     *  - 로그인시 Token을 QueryParameter에 포함하여 Web으로 Redirect
     *    - weburl/main?token=token
     * APP
     *  - app 커스텀 스킴으로 Redirect
     *   - app://callback?token=token
     */
    @GetMapping("/token")
    public ResponseEntity<?> getToken(
            @RequestParam
            Long memberId,
            @RequestParam
            String client
            ) {
        Token token = createTokenUseCase.create(memberId);
        HttpHeaders headers = new HttpHeaders();
        if ("app".equals(client)) {
            headers.setLocation(URI.create(appCustomScheme + APP_LOGIN_PATH + token.convertToQueryParam()));
        }
        else {
            headers.setLocation(URI.create(webUrl + WEB_LOGIN_PATH + token.convertToQueryParam()));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
