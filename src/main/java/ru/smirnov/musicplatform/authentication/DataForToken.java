package ru.smirnov.musicplatform.authentication;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.smirnov.musicplatform.entity.auxiliary.enums.AccountStatus;

import java.util.List;

@Data @Builder
public class DataForToken implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;
    private List<SimpleGrantedAuthority> authorities;

    private Long accountId;
    private String role;
    private Long entityId;


    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return enabled; }

}
