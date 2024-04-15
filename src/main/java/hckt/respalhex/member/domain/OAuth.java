package hckt.respalhex.member.domain;

import hckt.respalhex.member.domain.converter.Provider;
import hckt.respalhex.member.domain.converter.ProviderConverter;
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
@Table(name="OAUTH")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OAuth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name= "MEMBER_ID")
    private Member member;
    //소셜 타입
    @Convert(converter = ProviderConverter.class)
    @Column(columnDefinition = "varchar(10)")
    private Provider provider;

    private Boolean isDeleted;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private LocalDateTime deletedDate;

    public static OAuth create(Member member, Provider provider) {
        OAuth oAuth = new OAuth();
        oAuth.member = member;
        oAuth.provider = provider;
        return oAuth;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }
}
