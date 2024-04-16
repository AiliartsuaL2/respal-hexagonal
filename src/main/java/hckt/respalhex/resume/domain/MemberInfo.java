package hckt.respalhex.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {
    private String email;
    private String nickName;
    private String picture;

    public MemberInfo(String email, String nickName, String picture) {
        this.email = email;
        this.nickName = nickName;
        this.picture = picture;
    }
}
