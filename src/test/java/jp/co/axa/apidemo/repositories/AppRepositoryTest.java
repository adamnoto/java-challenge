package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = "test")
public class AppRepositoryTest {
    @Autowired
    private AppRepository appRepository;

    @Test
    public void save_AuthoritiesHappyPath_ShouldBeRetrievableAsList() {
        // When
        App app = new App("coolapp");
        appRepository.save(app);

        // Then
        then(app.getId()).isNotNull();
        then(app.getRights()).isEmpty();

        // When (2) -- add some rights
        app.addRights(Arrays.asList("READ", "WRITE", "DELETE"));
        appRepository.save(app);
        app = appRepository.findBySecretUserName(app.getSecretUserName()).get();

        // Then
        then(app.getRights()).containsExactlyInAnyOrder("READ", "WRITE", "DELETE");

        // When (3) -- remove some rights
        app.removeRights(Arrays.asList("WRITE"));
        appRepository.save(app);
        app = appRepository.findBySecretUserName(app.getSecretUserName()).get();

        // Then
        then(app.getRights()).containsExactlyInAnyOrder("READ", "DELETE");
    }
}
