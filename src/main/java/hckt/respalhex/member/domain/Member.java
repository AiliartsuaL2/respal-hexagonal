package hckt.respalhex.member.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public record Member(Long id, String email) {
}
