package hckt.respalhex.resume.application.dto.response;

import hckt.respalhex.resume.domain.Resume;
import hckt.respalhex.resume.domain.ResumeFile;

import java.util.List;

public record GetResumeResponseDto(String title, List<String> filePath, Integer views, String memberNickName, String memberPicture) {
    public static GetResumeResponseDto ofDetail(Resume resume, List<ResumeFile> resumeFile) {
        resume.view();
        return new GetResumeResponseDto(
                resume.getTitle(),
                resumeFile.stream().map(ResumeFile::getAccessUrl).toList(),
                resume.getViews(),
                resume.getMemberInfo().getNickName(),
                resume.getMemberInfo().getPicture()
        );
    }
}
