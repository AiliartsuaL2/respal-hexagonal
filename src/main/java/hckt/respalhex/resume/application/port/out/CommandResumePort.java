package hckt.respalhex.resume.application.port.out;

import hckt.respalhex.resume.domain.Resume;

public interface CommandResumePort {
    void create(Resume resume);
}
