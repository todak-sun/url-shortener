package foo.study.url.domain.repositories;

import foo.study.url.domain.entities.OriginURL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class OriginURLRepositoryTest {

    @Autowired
    OriginURLRepository originURLRepository;

    @DisplayName("OriginURL 을 저장하면 아이디, 생성일, 수정일 값이 들어간다.")
    @Test
    public void save_test() {
        //given
        String url = "https://www.naver.com";
        OriginURL newUrl = OriginURL.builder()
                .url(url)
                .build();

        //when
        OriginURL savedOriginUrl = originURLRepository.save(newUrl);

        //then
        assertEquals(url, savedOriginUrl.getUrl());
        assertNotNull(savedOriginUrl.getId());
        assertNotNull(savedOriginUrl.getCreatedDateTime());
        assertNotNull(savedOriginUrl.getUpdatedDateTime());
    }

}