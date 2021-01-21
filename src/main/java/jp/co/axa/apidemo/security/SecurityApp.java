package jp.co.axa.apidemo.security;

import jp.co.axa.apidemo.entities.App;
import jp.co.axa.apidemo.entities.AppRight;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Decorate the {@link jp.co.axa.apidemo.entities.App} class for
 * the purpose of authentication
 */
@AllArgsConstructor
public class SecurityApp implements UserDetails {
    private final App app;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return app.getRights()
            .stream()
            .map(AppRight::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return app.getSecretPassword();
    }

    @Override
    public String getUsername() {
        return app.getSecretUserName();
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
