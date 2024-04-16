package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.resume.domain.ResumeFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeFileRepository extends JpaRepository<ResumeFile, Long> , ResumeFileRepositoryCustom {
}
