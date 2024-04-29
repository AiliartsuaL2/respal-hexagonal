package hckt.respalhex.resume.domain;

import hckt.respalhex.resume.exception.ErrorMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long resumeId;
    private Long memberId;
    @Transient
    private Resume resume;

    public Tag(Resume resume, Long memberId) {
        if (resume.isOwner(memberId)) {
            throw new IllegalStateException(ErrorMessage.CAN_NOT_TAG_OWN_RESUME_EXCEPTION.getMessage());
        }
        if (resume.isTaggedMember(memberId)) {
            throw new IllegalStateException(ErrorMessage.ALREADY_TAGGED_MEMBER_EXCEPTION.getMessage());
        }
        this.resumeId = resume.getId();
        this.memberId = memberId;
        this.resume = resume;
    }
}
