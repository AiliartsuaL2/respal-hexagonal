package hckt.respalhex.member.domain;

import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static Member create(CreateMemberRequestDto requestDto) {
        Member member = new Member();
        member.id = requestDto.id();
        return member;
    }
}
