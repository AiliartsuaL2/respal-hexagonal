package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.domain.Member;
import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> loadMember(Long id);
}
