package foo.study.url.service;

import foo.study.url.domain.entities.OriginURL;
import foo.study.url.domain.entities.RequestLog;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.domain.repositories.OriginURLRepository;
import foo.study.url.domain.repositories.RequestLogRepository;
import foo.study.url.domain.repositories.ShortURLRepository;
import foo.study.url.exception.NotFoundException;
import foo.study.url.web.dto.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class UrlServiceTest {

    @Autowired
    UrlService urlService;

    @Autowired
    OriginURLRepository originURLRepository;

    @Autowired
    ShortURLRepository shortURLRepository;

    @Autowired
    RequestLogRepository requestLogRepository;

    @Autowired
    EntityManager em;

    @Transactional
    @DisplayName("새로운 URL을 요청받으면, 줄인 URL을 반환한다.")
    @Test
    public void create_test() {
        //given
        String url = "https://www.naver.com";

        //when
        ShortURL result = urlService.createShortURL(url);

        //then
        assertNotNull(result);
        assertNotNull(result.getPath());
        assertEquals(url, result.getOriginURL().getUrl());
    }

    @Transactional
    @DisplayName("같은 url의 경우 중복된 OriginURL을 생성하지 않는다.")
    @Test
    public void create_duplicate_originURL() {
        //given
        String url = "https://www.naver.com";

        //when
        for (int i = 0; i < 10; i++) {
            urlService.createShortURL(url);
        }

        //then
        assertEquals(1, originURLRepository.findAllByUrl(url).size());
    }


    @Transactional
    @DisplayName("같은 url로 요청이 오더라도, 매번 다른 path를 가진 shortUrl을 생성한다.")
    @Test
    public void create_always_different_short_url() {
        //given
        String url = "https://www.naver.com";

        //when
        for (int i = 0; i < 10; i++) {
            urlService.createShortURL(url);
        }

        //then
        OriginURL originURL = originURLRepository.findByUrl(url).get();
        List<ShortURL> shortURLS = originURL.getShortURLS();
        assertEquals(shortURLS.stream().map(ShortURL::getPath).count(),
                shortURLS.stream().map(ShortURL::getPath).distinct().count());
    }

    @Transactional
    @DisplayName("동일한 URL에 생성된 ShortenURL 의 개수 만큼 shortenCount 값이 채워진다.")
    @Test
    public void has_count_as_short_urls_amount() {

        //given
        String url = "https://www.naver.com";

        //when
        for (int i = 0; i < 10; i++) {
            urlService.createShortURL(url);
        }

        //then
        OriginURL originURL = originURLRepository.findByUrl(url).get();
        assertEquals(originURL.getShortURLS().size(), originURL.getShortenCount());
    }

    @Transactional
    @DisplayName("줄인 URL의 Path를 기준으로 OriginURL 정보를 가진 ShortURL을 가져온다")
    @Test
    public void fetch_shortURL_with_originURL_by_path() {
        //given
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);
        String path = shortURL.getPath();

        //when
        ShortURL shortURLWithOriginURL = urlService.fetchShortURLWithOriginURL(path);

        //then
        assertEquals(url, shortURLWithOriginURL.getOriginURL().getUrl());
        assertEquals(path, shortURLWithOriginURL.getPath());
    }

    @Transactional
    @DisplayName("줄인 URL에 대한 로그를 저장한다.")
    @Test
    public void save_log() {
        //given
        String url = "https://www.naver.com";
        String ip = "127.0.0.1";
        String referer = "https://google.com";

        ShortURL shortURL = urlService.createShortURL(url);
        ClientInfo clientInfo = ClientInfo.builder()
                .ip(ip)
                .referer(referer)
                .build();

        //when
        RequestLog requestLog = urlService.saveLog(shortURL, clientInfo);

        //then
        assertEquals(ip, requestLog.getIp());
        assertEquals(referer, requestLog.getReferer());
        assertNotNull(requestLog.getId());
    }

    @Transactional
    @DisplayName("줄인 URL에 로그를 저장하면, 저장된 로그 수 만큼 requestCount 가 증가한다.")
    @Test
    public void short_url_request_count_equals_log_counts() {
        //given
        int LOG_SIZE = 10;
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);

        //when
        IntStream.range(0, LOG_SIZE).mapToObj(n -> "127.0.0." + n)
                .forEach(ip ->
                        urlService.saveLog(shortURL, ClientInfo.builder()
                                .ip(ip)
                                .referer("https://google.com")
                                .build()));
        em.flush();
        em.clear();

        ShortURL result = shortURLRepository.findById(shortURL.getId()).get();
        //then
        assertEquals(result.getRequestLogs().size(), result.getRequestCount());
    }

    @Transactional
    @DisplayName("저장된 shortURL 을 모두 조회한다.")
    @Test
    public void fetch_all_saved_short_urls() {
        int SIZE = 10;
        //given
        IntStream.range(0, SIZE).mapToObj(n -> "https://wwww.naver.com/" + n)
                .collect(Collectors.toList())
                .forEach(url -> urlService.createShortURL(url));
        //when
        List<ShortURL> shortURLS = urlService.fetchAllShortenURLs();

        //then
        assertEquals(SIZE, shortURLS.size());
        shortURLS.forEach(shortURL -> assertNotNull(shortURL.getOriginURL()));
    }

    @Transactional
    @DisplayName("생성된 path에 요청한 로그를 모두 조회한다.")
    @Test
    public void fetch_short_url_with_logs() {
        //given
        int LOG_SIZE = 10;
        ShortURL shortURL = urlService.createShortURL("https://www.naver.com");

        IntStream.range(0, LOG_SIZE).mapToObj(n -> "127.0.0." + n)
                .forEach(ip -> urlService.saveLog(shortURL, ClientInfo.builder()
                        .ip(ip)
                        .referer("https://google.com")
                        .build()));

        //when
        ShortURL fetchedShortURL = urlService.fetchShortURLWithLogsByPath(shortURL.getPath());

        //then
        assertNotNull(fetchedShortURL);
        assertEquals(LOG_SIZE, fetchedShortURL.getRequestLogs().size());

        RequestLog requestLog = fetchedShortURL.getRequestLogs().get(0);
        assertNotNull(requestLog.getId());
        assertNotNull(requestLog.getIp());
        assertNotNull(requestLog.getReferer());
    }

    @Test
    @DisplayName("없는 주소를 찾으려고 하면 NotFoundException 을 내보낸다.")
    public void fetchShortURLWithLogsByPath_not_exist() {
        assertThrows(NotFoundException.class, () -> {
            urlService.fetchShortURLWithLogsByPath("UNKNOWN");
        });
    }

    @Test
    @DisplayName("없는 주소를 찾으려고 하면 NotFoundException 을 내보낸다.")
    public void fetchShortURLWithOriginURL_not_exist() {
        assertThrows(NotFoundException.class, () -> {
            urlService.fetchShortURLWithOriginURL("UNKNOWN");
        });
    }

}