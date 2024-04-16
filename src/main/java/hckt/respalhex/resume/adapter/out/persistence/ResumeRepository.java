package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.resume.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {
}
