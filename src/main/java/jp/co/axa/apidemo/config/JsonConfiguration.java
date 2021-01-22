package jp.co.axa.apidemo.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {
    /**
     * This configuration enable the JSON serializer to work
     * better with Hibernate
     * @return an instance of {@link Hibernate5Module}
     */
    @Bean
    public com.fasterxml.jackson.databind.Module hibernateModule() {
        return new Hibernate5Module();
    }
}
