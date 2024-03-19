package hckt.respalhex.auth.application.port.service;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;
import hckt.respalhex.auth.application.port.in.CreateTokenUseCase;
import hckt.respalhex.auth.application.port.in.ExtractPayloadUseCase;
import hckt.respalhex.auth.application.port.in.RenewAccessTokenUseCase;
import hckt.respalhex.auth.application.port.in.SignInUseCase;
import hckt.respalhex.auth.application.port.out.CommandRefreshTokenPort;
import hckt.respalhex.auth.application.port.out.LoadRefreshTokenPort;
import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;
import hckt.respalhex.auth.application.port.service.provider.CreateTokenProvider;
import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import hckt.respalhex.auth.domain.Token;
import hckt.respalhex.auth.exception.InvalidTokenException;
import hckt.respalhex.auth.exception.ErrorMessage;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class JwtService implements CreateTokenUseCase, ExtractPayloadUseCase, RenewAccessTokenUseCase, SignInUseCase {
    private final CommandRefreshTokenPort commandRefreshTokenPort;
    private final LoadRefreshTokenPort loadRefreshTokenPort;
    private final CreateTokenProvider createTokenProvider;
    private final GetTokenInfoProvider getTokenInfoProvider;
    private final LoadMemberInfoPort loadMemberInfoPort;

    @Override
    @Transactional
    public Token create(Long memberId) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }
        String accessToken = createTokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = createRefreshToken(memberId);
        return new Token(accessToken, refreshToken);
    }

    @Override
    public Long extractPayload(String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_ACCESS_TOKEN_EXCEPTION.getMessage());
        }
        getTokenInfoProvider.validateAndThrow(accessToken);
        return Long.valueOf(getTokenInfoProvider.getPayload(accessToken));
    }

    @Override
    public String renewAccessToken(String requestRefreshToken) {
        if (ObjectUtils.isEmpty(requestRefreshToken)) {
           throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage());
        }

        Long memberId = extractPayload(requestRefreshToken);

        // refresh 토큰 DB 검증
        String refreshToken = loadRefreshTokenPort.findByKeyId(memberId).orElseThrow(
                () -> new InvalidTokenException(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage()));
        // 만료 시간 체크
        getTokenInfoProvider.validateAndThrow(refreshToken);

        return createTokenProvider.createAccessToken(String.valueOf(memberId));
    }

    // 회원 도메인과 통신하여 회원 아이디 조회 후 토큰 발급
    @Override
    @Transactional
    public LogInResponseDto signIn(LogInRequestDto requestDto) {
        Long memberId = loadMemberInfoPort.signIn(requestDto);
        if (memberId == null) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_MEMBER_EXCEPTION.getMessage());
        }
        Token token = this.create(memberId);
        return LogInResponseDto.create(token);
    }

    private String createRefreshToken(Long memberId) {
        Optional<String> foundRefreshToken = loadRefreshTokenPort.findByKeyId(memberId);
        if (foundRefreshToken.isPresent()) {
            String existRefreshToken = foundRefreshToken.get();
            if (getTokenInfoProvider.isValid(existRefreshToken)) {
                return existRefreshToken;
            }
            commandRefreshTokenPort.delete(existRefreshToken);
        }
        String refreshToken = createTokenProvider.createRefreshToken(String.valueOf(memberId));
        commandRefreshTokenPort.create(memberId, refreshToken);
        return refreshToken;
    }
}
