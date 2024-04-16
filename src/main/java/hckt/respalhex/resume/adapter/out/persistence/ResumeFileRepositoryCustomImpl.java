package hckt.respalhex.resume.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hckt.respalhex.resume.domain.ResumeFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hckt.respalhex.resume.domain.QResumeFile.resumeFile;

@RequiredArgsConstructor
@Repository
public class ResumeFileRepositoryCustomImpl implements ResumeFileRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ResumeFile> findResumeFilesByResumeId(Long resumeId) {
        return queryFactory.selectFrom(resumeFile)
                .where(resumeFile.resumeId.eq(resumeId)
                        .and(resumeFile.isDeleted.eq(false))
                ).fetch();
    }
}
