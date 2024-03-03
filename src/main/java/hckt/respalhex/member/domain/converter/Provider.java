package hckt.respalhex.member.domain.converter;

import hckt.respalhex.global.converter.BaseEnumCode;
import hckt.respalhex.global.exception.ErrorMessage;

public enum Provider implements BaseEnumCode<String> {
    KAKAO("kakao"),
    GOOGLE("google"),
    GITHUB("github"),
    COMMON("common");

    Provider(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String getValue() {
        return this.value;
    }

    public static Provider findByValue(String value) {
        for(Provider provider : Provider.values()) {
            if(provider.value.equals(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION.getMessage());
    }

    public boolean isCommon() {
        return Provider.COMMON.equals(this);
    }
}
