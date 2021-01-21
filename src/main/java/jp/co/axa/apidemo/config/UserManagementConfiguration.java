package jp.co.axa.apidemo.config;

import jp.co.axa.apidemo.entities.App;
import jp.co.axa.apidemo.repositories.AppRepository;
import jp.co.axa.apidemo.security.SecurityApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserManagementConfiguration {
    @Autowired
    private AppRepository appRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String secretUserName) throws UsernameNotFoundException {
                App app = appRepository
                    .findBySecretUserName(secretUserName)
                    .orElseThrow(
                        () -> new UsernameNotFoundException("App with specified username can't be found")
                    );

                return new SecurityApp(app);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence raw, String encoded) {
                return encoded.equals(raw);
            }
        };
    }
}
