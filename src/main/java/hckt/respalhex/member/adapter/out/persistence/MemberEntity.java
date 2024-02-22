package hckt.respalhex.member.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String email;

    static MemberEntity create(Long id, String email) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.id = id;
        memberEntity.email = email;
        return memberEntity;
    }
}
