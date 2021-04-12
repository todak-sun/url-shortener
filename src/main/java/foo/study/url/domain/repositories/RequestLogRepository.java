package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.RequestLog;
import foo.study.url.domain.entities.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    List<RequestLog> findAllByShortURL(ShortURL shortURL);

}
