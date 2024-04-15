package hckt.respalhex.member.config;

import hckt.respalhex.member.adapter.out.communicate.dto.GithubResponseDto;
import hckt.respalhex.member.adapter.out.communicate.dto.GoogleResponseDto;
import hckt.respalhex.member.adapter.out.communicate.dto.KakaoResponseDto;
import hckt.respalhex.member.domain.converter.Provider;
import hckt.respalhex.member.exception.ErrorMessage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "oauth")
public class OAuth2ProviderProperties {
    private final ProviderInfo kakao;
    private final ProviderInfo google;
    private final ProviderInfo github;

    @ConstructorBinding
    public OAuth2ProviderProperties(ProviderInfo kakao, ProviderInfo google, ProviderInfo github) {
        this.kakao = kakao;
        this.google = google;
        this.github = github;
        kakao.setResponseType(KakaoResponseDto.class);
        google.setResponseType(GoogleResponseDto.class);
        github.setResponseType(GithubResponseDto.class);
    }

    public ProviderInfo get(Provider provider) {
        if(Provider.KAKAO.equals(provider)) {
            return kakao;
        }
        if(Provider.GITHUB.equals(provider)) {
            return github;
        }
        if(Provider.GOOGLE.equals(provider)) {
            return google;
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION.getMessage());
    }
}

