package gusil.mybox.domain;

import gusil.mybox.security.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String userId = UUID.randomUUID().toString();
    private String userName;
    private String userPassword;
    private Long maxUsage;
    private Long currentUsage;

    private Boolean enabled;

    private List<Role> roles;

    @Builder
    public User(String userName, String userPassword, Long maxUsage, Long currentUsage) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.maxUsage = maxUsage;
        this.currentUsage = currentUsage;
        this.enabled = true;
        this.roles = Collections.singletonList(Role.ROLE_USER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
