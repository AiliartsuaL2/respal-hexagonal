package hckt.respalhex.resume.application.service;

import hckt.respalhex.global.annotation.UseCase;
import hckt.respalhex.resume.application.dto.request.CreateResumeRequestDto;
import hckt.respalhex.resume.application.dto.request.UpdateResumeRequestDto;
import hckt.respalhex.resume.application.dto.response.GetResumeResponseDto;
import hckt.respalhex.resume.application.port.in.DeleteResumeUseCase;
import hckt.respalhex.resume.application.port.in.GetResumeUseCase;
import hckt.respalhex.resume.application.port.in.PostResumeUseCase;
import hckt.respalhex.resume.application.port.in.UpdateResumeUseCase;
import hckt.respalhex.resume.application.port.out.CommandResumeFilePort;
import hckt.respalhex.resume.application.port.out.CommandResumePort;
import hckt.respalhex.resume.application.port.out.LoadResumeFilePort;
import hckt.respalhex.resume.application.port.out.LoadResumePort;
import hckt.respalhex.resume.domain.Resume;
import hckt.respalhex.resume.domain.ResumeFile;
import hckt.respalhex.resume.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService implements PostResumeUseCase, GetResumeUseCase, UpdateResumeUseCase, DeleteResumeUseCase {
    private final CommandResumePort commandResumePort;
    private final LoadResumePort loadResumePort;
    private final LoadResumeFilePort loadResumeFilePort;
    private final CommandResumeFilePort commandResumeFilePort;

    @Override
    public void create(CreateResumeRequestDto requestDto) {
        Resume resume = new Resume(requestDto.title(), requestDto.memberId());
        ResumeFile resumeFile = new ResumeFile("", requestDto.fileName(), requestDto.filePath(), resume.getId());
        commandResumePort.create(resume);
        commandResumeFilePort.create(resumeFile);
    }

    @Override
    public void delete(Long resumeId, Long memberId) {
        Resume resume = loadResumePort.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        resume.delete(memberId);

        List<ResumeFile> resumeFiles = loadResumeFilePort.findResumeFilesByResumeId(resumeId);
        for (ResumeFile resumeFile : resumeFiles) {
            resumeFile.delete();
        }
    }

    @Override
    public GetResumeResponseDto view(Long resumeId, Long memberId) {
        Resume resume = loadResumePort.findResumeWithMemberInfo(resumeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        List<ResumeFile> resumeFiles = loadResumeFilePort.findResumeFilesByResumeId(resumeId);
        return GetResumeResponseDto.ofDetail(resume, resumeFiles);
    }

    @Override
    public void update(UpdateResumeRequestDto requestDto) {
        Resume resume = loadResumePort.findById(requestDto.resumeId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        resume.update(requestDto.title(), requestDto.memberId());
    }
}
