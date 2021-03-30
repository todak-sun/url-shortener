package foo.study.url.service;

import foo.study.url.domain.entities.OriginURL;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.domain.repositories.OriginURLRepository;
import foo.study.url.domain.repositories.RequestLogRepository;
import foo.study.url.web.dto.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class UrlServiceTest {

    @Autowired
    UrlService urlService;

    @Autowired
    OriginURLRepository originURLRepository;

    @Autowired
    RequestLogRepository requestLogRepository;

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

    @DisplayName("서버에서 생성한 줄어든 PATH로 요청시, 원래 URL을 반환한다.")
    @Test
    public void get_origin_url_by_path_test() {
        //given
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);
        String generatedPath = shortURL.getPath();

        ClientInfo clientInfo = ClientInfo.builder().build();

        //when
        String result = urlService.getOriginURLByPath(generatedPath, clientInfo);

        //then
        assertEquals(url, result);
    }

    @DisplayName("서버에서 생성한 PATH로 조회를 시도할 경우, 조회 요청 1건 당 로그 데이터 1건 을 남긴다.")
    @Test
    public void create_log_per_request() {
        int TRY = 10;
        //given
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);
        String generatedPath = shortURL.getPath();

        ClientInfo clientInfo = ClientInfo.builder().build();

        //when
        for (int i = 0; i < TRY; i++) {
            urlService.getOriginURLByPath(generatedPath, clientInfo);
        }

        //then
        assertEquals(TRY, requestLogRepository.findAllByShortURL(shortURL).size());
    }


}