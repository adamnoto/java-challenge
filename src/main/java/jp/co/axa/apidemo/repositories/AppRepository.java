package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.App;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRepository extends CrudRepository<App, Integer> {
    Optional<App> findBySecretUserName(String secretUserName);
}
