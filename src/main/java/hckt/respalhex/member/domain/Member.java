package hckt.respalhex.member.domain;

import lombok.Getter;

public record Member(
        @Getter
        Long id,
        @Getter
        String email
) {
}
