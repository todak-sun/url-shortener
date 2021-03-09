package foo.study.url.domain;

import foo.study.url.ifs.IdGenerator;
import foo.study.url.util.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UrlRepositoryTest {


    UrlRepository urlRepository;

    @BeforeEach
    public void setUp() {
        IdGenerator idGenerator = new UUIDGenerator();
        this.urlRepository = new UrlHashMapRepository(idGenerator);
    }

    @DisplayName("저장 테스트 해보자")
    @Test
    public void save_test() {
        Url url = new Url("https://www.naver.com");
        assertNull(url.getId());
        urlRepository.save(url);
        System.out.println(url.getId());
        assertNotNull(url.getId());
    }

}