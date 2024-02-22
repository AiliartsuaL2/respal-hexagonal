package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.domain.Member;

public interface LoadMemberPort {
    Member loadMember(Long id);
}
