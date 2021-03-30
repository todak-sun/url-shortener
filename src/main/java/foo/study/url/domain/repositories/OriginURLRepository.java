package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.OriginURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OriginURLRepository extends JpaRepository<OriginURL, Long> {

    List<OriginURL> findAllByUrl(String url);

    Optional<OriginURL> findByUrl(String url);

}
