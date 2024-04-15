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
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="OAUTH_INFO")
@EntityListeners(AuditingEntityListener.class)
public class OAuthInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36)
    private String uid;

    @Convert(converter = ProviderConverter.class)
    @Column(columnDefinition = "varchar(10)")
    private Provider provider;

    private String email;

    @Column(length = 2083)
    private String image;

    private String nickname;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public OAuthInfo(Provider provider, String email, String image, String nickname) {
        this.uid = UUID.randomUUID().toString().replace("-", "");
        this.provider = provider;
        this.email = email;
        this.image = image;
        this.nickname = nickname;
    }
}
