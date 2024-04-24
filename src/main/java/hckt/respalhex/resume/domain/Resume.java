package hckt.respalhex.resume.domain;

import com.querydsl.core.annotations.QueryProjection;
import hckt.respalhex.global.domain.BaseEntity;
import hckt.respalhex.resume.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer views;

    private Long memberId;

    private Boolean isDeleted;

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
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        if (this.memberId.longValue() != memberId.longValue()) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }

    public Resume(String title, Long memberId) {
        notNullValidation(title, ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());

        this.title = title;
        this.memberId = memberId;
        this.views = 0;
        this.isDeleted = false;
    }

    public void update(String title, Long memberId) {
        notNullValidation(title, ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        if (this.memberId.longValue() != memberId.longValue()) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }

        this.title = title;
    }
}
