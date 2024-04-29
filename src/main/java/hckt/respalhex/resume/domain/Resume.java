package hckt.respalhex.resume.domain;

import com.querydsl.core.annotations.QueryProjection;
import hckt.respalhex.global.domain.BaseEntity;
import hckt.respalhex.resume.exception.ErrorMessage;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    private Integer views;

    @Getter
    private Long memberId;

    @Getter
    private Boolean isDeleted;

    @Getter
    private LocalDateTime deletedDate;

    @Getter
    @Transient
    private MemberInfo memberInfo;

    // 참조변수 Getter 미할당
    @Transient
    private List<ResumeFile> resumeFiles;

    @Transient
    private List<Long> taggedMemberIds;

    public Resume(String title, Long memberId) {
        notNullValidation(title, ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());

        this.title = title;
        this.memberId = memberId;
        this.views = 0;
        this.isDeleted = false;
        this.resumeFiles = new ArrayList<>();
        this.taggedMemberIds = new ArrayList<>();
    }

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
        this.resumeFiles = new ArrayList<>();
        this.taggedMemberIds = new ArrayList<>();
    }

    public boolean isTaggedMember(Long memberId) {
        return this.taggedMemberIds.contains(memberId);
    }

    public boolean isOwner(Long memberId) {
        return this.memberId.equals(memberId);
    }

    public void addResumeFile(ResumeFile resumeFile) {
        this.resumeFiles.add(resumeFile);
    }

    public void tagMember(Long memberId) {
        if (isTaggedMember(memberId)) {
            throw new IllegalStateException(ErrorMessage.ALREADY_TAGGED_MEMBER_EXCEPTION.getMessage());
        }
        this.taggedMemberIds.add(memberId);
    }

    public void view(Long viewer) {
        // 본인 글인경우 조회수 미증가
        if (isOwner(viewer)) {
            return;
        }
        if (!isTaggedMember(viewer)) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_VIEW.getMessage());
        }
        this.views++;
    }

    public void delete(Long memberId) {
        if (this.isDeleted.booleanValue() == true) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        if (this.memberId.longValue() != memberId.longValue()) {
            throw new IllegalArgumentException(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
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
