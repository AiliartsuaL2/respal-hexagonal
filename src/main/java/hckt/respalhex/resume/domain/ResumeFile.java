package hckt.respalhex.resume.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ResumeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName; // 이미지 파일의 본래 이름

    private String storedName; // 이미지 파일이 S3에 저장될때 사용되는 이름

    private String accessUrl; // S3 내부 이미지에 접근할 수 있는 URL

    private Long resumeId;

    private Boolean isDeleted;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private LocalDateTime deletedDate;

    public void delete() {
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }

    public ResumeFile(String originName, String storedName, String accessUrl, Long resumeId) {
        this.originName = originName;
        this.storedName = storedName;
        this.accessUrl = accessUrl;
        this.resumeId = resumeId;
        this.isDeleted = false;
    }
}
