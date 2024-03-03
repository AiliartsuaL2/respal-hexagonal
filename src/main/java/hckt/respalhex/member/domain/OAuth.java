package hckt.respalhex.member.domain;

import hckt.respalhex.member.domain.converter.Provider;
import hckt.respalhex.member.domain.converter.ProviderConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="OAUTH")
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

    public static OAuth create(Member member, Provider provider) {
        OAuth oAuth = new OAuth();
        oAuth.member = member;
        oAuth.provider = provider;
        return oAuth;
    }
}
