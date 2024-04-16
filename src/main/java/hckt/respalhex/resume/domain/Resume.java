package hckt.respalhex.resume.domain;

import com.querydsl.core.annotations.QueryProjection;
import hckt.respalhex.resume.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer views;

    private Long memberId;

    private Boolean isDeleted;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private LocalDateTime deletedDate;

    @Transient
    private MemberInfo memberInfo;

    @Transient
    private List<ResumeFile> resumeFiles;


    @QueryProjection
    public Resume(Resume resume, String memberEmail, String memberNickName, String memberPicture){
        this.id = resume.id;
        this.title = resume.title;
        this.views = resume.views;
        this.memberId = resume.memberId;
        this.isDeleted = resume.isDeleted;
        this.createdDate = resume.createdDate;
        this.modifiedDate = resume.modifiedDate;
        this.deletedDate = resume.deletedDate;
        this.memberInfo = new MemberInfo(memberEmail, memberNickName, memberPicture);
    }

    public void view() {
        this.views++;
    }

    public void delete(Long memberId) {
        if (this.memberId.longValue() != memberId.longValue()) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }

    public Resume(String title, Long memberId) {
        this.title = title;
        this.memberId = memberId;
        this.views = 0;
        this.isDeleted = false;
    }

    public void update(String title, Long memberId) {
        if (this.memberId.longValue() != memberId.longValue()) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }
        this.title = title;
    }
}
