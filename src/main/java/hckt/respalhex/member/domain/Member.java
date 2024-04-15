package hckt.respalhex.member.domain;


import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {
    private static final String RANDOM_PICTURE_URL = "https://www.gravatar.com/avatar/";
    private static final String PICTURE_TYPE_PARAM = "?d=identicon";
    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 이메일 , 일반 로그인 식별자
    @Column(length = 50)
    private String email;
    // 비밀번호 , 암호화 되어 진행, 결과값은 항상 60
    @Column(length = 60)
    private String password;
    // 닉네임
    @Column(length = 20)
    private String nickname;
    @Column(length = 2083)
    private String picture;

    @OneToMany(mappedBy = "member")
    private List<OAuth> oauthList;
    private Boolean isDeleted;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private LocalDateTime deletedDate;

    public static Member create(PostMemberRequestDto requestDto) {
        String encodedPassword = B_CRYPT_PASSWORD_ENCODER.encode(requestDto.password());
        String checkedPicture = checkPicture(requestDto.picture());
        Member member = new Member();
        member.email = requestDto.email();
        member.nickname = requestDto.nickname();
        member.picture = checkedPicture;
        member.password = encodedPassword;
        member.oauthList = new ArrayList<>();
        return member;
    }

    public void addOAuth(OAuth oAuth) {
        if(oAuth == null) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_OAUTH_INFO_EXCEPTION.getMessage());
        }
        oauthList.add(oAuth);
    }

    public boolean matchPassword(String password) {
        return B_CRYPT_PASSWORD_ENCODER.matches(password, this.password);
    }

    private static String checkPicture(String picture) {
        if(ObjectUtils.isEmpty(picture)) {
            return String.join("",
                    RANDOM_PICTURE_URL,
                    UUID.randomUUID().toString().replace("-",""),
                    PICTURE_TYPE_PARAM);
        }
        return picture;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now();
    }
}

