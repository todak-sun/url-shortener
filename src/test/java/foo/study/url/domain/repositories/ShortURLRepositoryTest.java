package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.ShortURL;
import foo.study.url.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ShortURLRepositoryTest {

    @Autowired
    UrlService urlService;

    @Autowired
    ShortURLRepository shortURLRepository;

    @DisplayName("path에 해당하는 ShortURL과, OriginURL을 같이 조회하는 쿼리")
    @Test
    public void findByPathWithOriginUrl() {

        ShortURL shortURL = urlService.createShortURL("https://www.naver.com");

        ShortURL savedShortURL = shortURLRepository.findByPathWithOriginURL(shortURL.getPath()).get();

        assertNotNull(savedShortURL);
        assertNotNull(savedShortURL.getOriginURL());
    }

}