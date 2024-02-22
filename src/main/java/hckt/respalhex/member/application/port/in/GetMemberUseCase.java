package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.domain.Member;
import java.util.Optional;
public interface GetMemberUseCase {
    Optional<Member> getMember(Long id);
}
