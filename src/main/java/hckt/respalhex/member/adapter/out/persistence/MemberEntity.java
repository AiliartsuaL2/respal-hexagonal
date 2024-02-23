package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 이메일 , 일반 로그인 식별자
    @Column(length = 50)
    private String email;
    // 비밀번호 , 암호화 되어 진행, 결과값은 항상 60
    @Column(length = 60)
    private String password;
    // 닉네임
    @Column(length = 20)
    private String nickname;
    @Column(length = 2083)
    private String picture;

    static MemberEntity create(Member member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.email = member.email();
        memberEntity.password = member.password();
        memberEntity.nickname = member.nickname();
        memberEntity.picture = member.picture();
        return memberEntity;
    }
}
