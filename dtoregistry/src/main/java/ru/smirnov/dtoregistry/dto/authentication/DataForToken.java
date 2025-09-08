package ru.smirnov.dtoregistry.dto.authentication;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data @Builder @Setter
public class DataForToken implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;
    private List<SimpleGrantedAuthority> authorities;

    private Long accountId;
    private String role;
    private Long entityId;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
