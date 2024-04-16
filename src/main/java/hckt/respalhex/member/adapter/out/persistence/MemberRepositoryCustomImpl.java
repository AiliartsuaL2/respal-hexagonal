package hckt.respalhex.member.adapter.out.persistence;

import static hckt.respalhex.member.domain.QMember.member;
import static hckt.respalhex.member.domain.QOAuth.oAuth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findMemberByEmailAndProvider(String email, Provider provider) {
        Member result = queryFactory.select(member)
                .from(member)
                .innerJoin(member.oauthList, oAuth)
                .where(member.email.eq(email)
                        .and(oAuth.provider.eq(provider)))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
