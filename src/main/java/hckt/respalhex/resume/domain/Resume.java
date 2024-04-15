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

    public void view() {
        this.views++;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }

    public Resume(String title, Long memberId) {
        this.title = title;
        this.memberId = memberId;
        this.views = 0;
        this.isDeleted = false;
    }
}
