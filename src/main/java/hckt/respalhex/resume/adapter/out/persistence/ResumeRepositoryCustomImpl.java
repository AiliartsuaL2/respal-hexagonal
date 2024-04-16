package hckt.respalhex.resume.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hckt.respalhex.resume.domain.QResume;
import hckt.respalhex.resume.domain.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static hckt.respalhex.member.domain.QMember.member;
import static hckt.respalhex.resume.domain.QResume.resume;
import static hckt.respalhex.resume.domain.QResumeFile.resumeFile;

@RequiredArgsConstructor
@Repository
public class ResumeRepositoryCustomImpl implements ResumeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Resume> findResumeWithMemberInfo(Long resumeId) {
        Resume result = queryFactory.select(QResume.create(resume, member.email, member.nickname, member.picture))
                .from(resume)
                .join(member).on(resume.memberId.eq(member.id))
                .where(resume.id.eq(resumeId)
                        .and(resumeFile.isDeleted.eq(false)))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
