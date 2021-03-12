package foo.study.url.domain;

import foo.study.url.ifs.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MemoryRepository<ShortenURL, String> {

    boolean existsByUrl(String url);

}
