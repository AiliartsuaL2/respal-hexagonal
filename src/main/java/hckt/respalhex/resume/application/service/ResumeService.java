package hckt.respalhex.resume.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import hckt.respalhex.resume.exception.AwsS3Exception;
import hckt.respalhex.resume.exception.ErrorMessage;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@Transactional(readOnly = true)
public class ResumeService implements PostResumeUseCase, GetResumeUseCase, UpdateResumeUseCase, DeleteResumeUseCase {
    private final CommandResumePort commandResumePort;
    private final LoadResumePort loadResumePort;
    private final LoadResumeFilePort loadResumeFilePort;
    private final CommandResumeFilePort commandResumeFilePort;
    private final AmazonS3Client amazonS3Client;
    private final String bucketName;

    public ResumeService(CommandResumePort commandResumePort, LoadResumePort loadResumePort,
                         LoadResumeFilePort loadResumeFilePort, CommandResumeFilePort commandResumeFilePort,
                         AmazonS3Client amazonS3Client, @Value("${s3.bucket.name}") String bucketName) {
        this.commandResumePort = commandResumePort;
        this.loadResumePort = loadResumePort;
        this.loadResumeFilePort = loadResumeFilePort;
        this.commandResumeFilePort = commandResumeFilePort;
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
    }

    @Override
    @Transactional
    public void create(CreateResumeRequestDto requestDto, MultipartFile multipartFile) {
        try {
            String registerName = convertToRegisterName(requestDto.fileName());
            String accessUrl = registerResumeFile(multipartFile, registerName);
            Resume resume = new Resume(requestDto.title(), requestDto.memberId());
            ResumeFile resumeFile = new ResumeFile(requestDto.fileName(), registerName, accessUrl, resume);

            commandResumePort.create(resume);
            commandResumeFilePort.create(resumeFile);
        } catch (IOException e) {
            // S3 bucket Access 에러시 IOException
            throw new AwsS3Exception(ErrorMessage.AWS_S3_ACCESS_ERROR_EXCEPTION.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long resumeId, Long memberId) {
        Resume resume = loadResumePort.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        resume.delete(memberId);

        List<ResumeFile> resumeFiles = loadResumeFilePort.findResumeFilesByResumeId(resumeId);
        for (ResumeFile resumeFile : resumeFiles) {
            resumeFile.delete(memberId);
        }
    }

    @Override
    public GetResumeResponseDto view(Long resumeId, Long memberId) {
        Resume resume = loadResumePort.findResumeWithMemberInfo(resumeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        List<ResumeFile> resumeFiles = loadResumeFilePort.findResumeFilesByResumeId(resumeId);
        resume.view();
        return GetResumeResponseDto.ofDetail(resume, resumeFiles);
    }

    @Override
    @Transactional
    public void update(UpdateResumeRequestDto requestDto) {
        Resume resume = loadResumePort.findById(requestDto.resumeId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage()));
        resume.update(requestDto.title(), requestDto.memberId());
    }

    private String registerResumeFile(MultipartFile multipartFile, String registerName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        amazonS3Client.putObject(bucketName, registerName, multipartFile.getInputStream(), objectMetadata);

        return amazonS3Client.getUrl(bucketName, registerName).toString();
    }

    // 이미지 파일의 확장자를 추출하는 메소드
    private String extractExtension(String originName) {
        String[] fullName = originName.split(".");
        if (fullName.length < 2) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_EXTENSION_EXCEPTION.getMessage());
        }
        return fullName[(fullName.length-1)];
    }

    // 저장 파일명 생성 메서드
    private String convertToRegisterName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }
}
