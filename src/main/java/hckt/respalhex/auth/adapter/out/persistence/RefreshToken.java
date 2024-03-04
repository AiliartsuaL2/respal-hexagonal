package hckt.respalhex.auth.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REFRESH_TOKEN")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class RefreshToken {
    @Id
    private Long id;
    private Long keyId;
    private String refreshToken;

    public RefreshToken(Long keyId, String refreshToken) {
        this.keyId = keyId;
        this.refreshToken = refreshToken;
    }
}
