package hckt.respalhex.member.domain;


import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
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

    public static Member create(PostMemberRequestDto requestDto) {
        String encodedPassword = B_CRYPT_PASSWORD_ENCODER.encode(requestDto.password());
        String checkedPicture = checkPicture(requestDto.picture());
        Member member = new Member();
        member.email = requestDto.email();
        member.nickname = requestDto.nickname();
        member.picture = checkedPicture;
        member.password = encodedPassword;
        return member;
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
}
