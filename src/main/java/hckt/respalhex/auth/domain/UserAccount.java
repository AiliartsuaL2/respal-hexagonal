package hckt.respalhex.auth.domain;

import hckt.respalhex.auth.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_account")
@Getter
public class UserAccount implements UserDetails {
    @Id @GeneratedValue
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable=false)
    private RoleType roleType;

    public static UserAccount create(Long memberId, String roleType) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }
        if (ObjectUtils.isEmpty(roleType)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_ROLE_TYPE_EXCEPTION.getMessage());
        }

        UserAccount userAccount = new UserAccount();
        userAccount.memberId = memberId;
        if("user".equals(roleType)) {
            userAccount.roleType = RoleType.ROLE_USER;
        }
        if("admin".equals(roleType)) {
            userAccount.roleType = RoleType.ROLE_ADMIN;
        }
        return userAccount;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.roleType);
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.memberId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
