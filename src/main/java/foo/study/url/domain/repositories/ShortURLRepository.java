package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {

    Optional<ShortURL> findByPath(String path);

    @Query("select s from ShortURL s join fetch s.originURL where s.path = :path")
    Optional<ShortURL> findByPathWithOriginURL(String path);

    @Query("select s from ShortURL s join fetch s.originURL")
    List<ShortURL> findAllWithOriginURL();

    boolean existsByPath(String path);

    @Query("select s from ShortURL s join fetch s.originURL join fetch s.requestLogs where s.path = :path")
    Optional<ShortURL> findWithRequestLogsByPath(String path);
}
