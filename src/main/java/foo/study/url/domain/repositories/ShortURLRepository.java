package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {

    Optional<ShortURL> findByPath(String path);

    boolean existsByPath(String path);

}
