package hckt.respalhex.resume.domain;

import hckt.respalhex.global.domain.BaseEntity;
import hckt.respalhex.resume.exception.ErrorMessage;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName; // 이미지 파일의 본래 이름

    private String registeredName; // 이미지 파일이 S3에 저장될때 사용되는 이름

    private String accessUrl; // S3 내부 이미지에 접근할 수 있는 URL

    private Long resumeId;

    private Boolean isDeleted;

    private LocalDateTime deletedDate;

    @Transient
    Resume resume;

    public void delete(Long memberId) {
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        if (!memberId.equals(this.resume.getMemberId())) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }

    public ResumeFile(String originName, String registeredName, String accessUrl, Resume resume) {
        notNullValidation(originName, ErrorMessage.NOT_EXIST_ORIGIN_NAME_EXCEPTION.getMessage());
        notNullValidation(registeredName, ErrorMessage.NOT_EXIST_REGISTERED_NAME_EXCEPTION.getMessage());
        notNullValidation(accessUrl, ErrorMessage.NOT_EXIST_ACCESS_URL_EXCEPTION.getMessage());
        notNullValidation(resume, ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());

        this.originName = originName;
        this.registeredName = registeredName;
        this.accessUrl = accessUrl;
        this.resume = resume;
        this.resumeId = resume.getId();
        this.isDeleted = false;
    }
}
