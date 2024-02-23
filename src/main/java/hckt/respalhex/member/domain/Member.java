package hckt.respalhex.member.domain;

import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

public record Member(
        Long id,
        String email,
        String password,
        String nickname,
        String picture
) {
        private static final String RANDOM_PICTURE_URL = "https://www.gravatar.com/avatar/";
        private static final String PICTURE_TYPE_PARAM = "?d=identicon";
        private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

        public static Member of(PostMemberRequestDto requestDto) {
                String encodedPassword = B_CRYPT_PASSWORD_ENCODER.encode(requestDto.password());
                String checkedPicture = checkPicture(requestDto.picture());
                return new Member(null, requestDto.email(), encodedPassword, requestDto.nickname(), checkedPicture);
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
