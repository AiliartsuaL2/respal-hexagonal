package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> loadMember(Long id);
    Optional<Member> loadMemberByEmailAndProvider(String email, Provider provider);
}
