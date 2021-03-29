package foo.study.url.domain;

import foo.study.url.ifs.IdGenerator;
import foo.study.url.util.HashAndEncode64IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UrlHashMapRepositoryTest {

    UrlRepository urlRepository;

    @BeforeEach
    public void setUp() {
        IdGenerator idGenerator = new HashAndEncode64IdGenerator();
        this.urlRepository = new UrlHashMapRepository(idGenerator);
    }

    @DisplayName("저장할 경우 자동으로 ID가 부여된다.")
    @Test
    public void save_test() {
        //given
        ShortenURL shortenURL = ShortenURL.builder().url("www.naver.com").build();

        //when & then
        assertNull(shortenURL.getId());
        urlRepository.save(shortenURL);
        assertNotNull(shortenURL.getId());
    }

    @DisplayName("같은 URL을 여러번 저장해도 항상 같은 ID를 저장한다.")
    @Test
    public void always_return_same_id() {

        //given
        String targetURL = "https//www.google.com";
        String expected = urlRepository.save(ShortenURL.builder().url(targetURL).build()).getId();

        //when
        IntStream.range(0, 100).mapToObj((n) -> ShortenURL.builder().url(targetURL).build())
                .forEach(url -> {
                    //then
                    assertEquals(expected, urlRepository.save(url).getId());
                });
    }

    @DisplayName("다른 URL을 저장하면 항상 다른 ID가 저장된다.")
    @Test
    public void each_url_return_has_own_id() {
        //given
        AtomicReference<String> ref = new AtomicReference<>("");
        Stream<String> urlStream = IntStream.range(0, 100).mapToObj(n -> "www." + UUID.randomUUID().toString().replaceAll("-", "") + ".com");

        //when
        urlStream.forEach(url -> {
            String newId = urlRepository.save(ShortenURL.builder().url(url).build()).getId();
            assertNotEquals(ref.get(), newId);
            ref.set(newId);
        });
    }

    @DisplayName("N개를 저장한 후, findAll로 모두 조회하면, N개가 나온다.")
    @Test
    public void always_return_same_amount_like_save() {
        //given
        urlRepository.deleteAll();
        int N = 100;

        //when
        IntStream.range(0, N).mapToObj(n -> "www." + UUID.randomUUID().toString().replaceAll("-", "") + ".com")
                .forEach(url -> urlRepository.save(ShortenURL.builder().url(url).build()));

        //then
        assertEquals(N, urlRepository.findAll().size());
    }


}