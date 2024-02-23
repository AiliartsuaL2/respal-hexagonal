package hckt.respalhex.member.domain;

import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

public record Member(
        @Getter
        Long id,
        @Getter
        String email,
        @Getter
        String password,
        @Getter
        String nickname,
        @Getter
        String picture
) {
        private static String RANDOM_PICTURE_URL = "https://www.gravatar.com/avatar/";
        private static String PICTURE_TYPE_PARAM = "?d=identicon";
        private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

        @Builder
        public Member(Long id, String email, String password, String nickname, String picture) {
                this.id = id;
                this.email = email;
                this.password = B_CRYPT_PASSWORD_ENCODER.encode(password);
                this.nickname = nickname;
                this.picture = checkPicture(picture);
        }

        public boolean matchPassword(String password) {
                return B_CRYPT_PASSWORD_ENCODER.matches(password, this.password);
        }

        private String checkPicture(String picture) {
                if(ObjectUtils.isEmpty(picture)) {
                        return String.join("",
                                RANDOM_PICTURE_URL,
                                UUID.randomUUID().toString().replace("-",""),
                                PICTURE_TYPE_PARAM);
                }
                return picture;
        }
}
