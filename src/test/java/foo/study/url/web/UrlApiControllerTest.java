package foo.study.url.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.service.UrlService;
import foo.study.url.web.dto.ClientInfo;
import foo.study.url.web.dto.UrlDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@DisplayName("UrlApiController 테스트")
@SpringBootTest
class UrlApiControllerTest {

    @Autowired
    WebApplicationContext ctx;

    @Autowired
    UrlService urlService;


    MockMvc mvc;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .alwaysDo(print())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @DisplayName("URL을 요청하면, 서버에서 생성한 URL이 돌아온다.")
    @Test
    public void create_test() throws Exception {
        //given
        String url = "https://www.naver.com";
        String body = objectMapper.writeValueAsString(new UrlDto.Req.Create(url));
        log.info("body : {}", body);

        //when
        ResultActions when = mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body));

        //then
        when.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").exists());
    }

    @DisplayName("잘못된 URL을 요청하면, 에러메시지를 반환한다.")
    @Test
    public void create_test_fail() throws Exception {
        //given
        String invalidUrl = "htt:www.naver.com";
        String body = objectMapper.writeValueAsString(new UrlDto.Req.Create(invalidUrl));
        log.info("body : {}", body);

        //when
        ResultActions when = mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body));

        //then
        when.andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$" ).isArray())
            .andExpect(jsonPath("$[0].rejectedValue"  ).exists())
                .andExpect(jsonPath("$[0].invalidField"  ).exists())
                .andExpect(jsonPath("$[0].message"  ).exists())
        ;
    }

    @DisplayName("서버에서 생성한 URL로 요청하면, 원래 주소로 redirect 해준다.")
    @Test
    public void redirect_test() throws Exception {
        //given
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);
        String path = shortURL.getPath();

        //when
        ResultActions when = mvc.perform(get("/" + path));

        //then
        when.andExpect(status().isSeeOther())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.LOCATION, url));
    }

    @DisplayName("서버에 존재하는 줄인 URL을 모두 조회한다.")
    @Test
    public void fetch_all_short_urls() throws Exception {
        //given
        int SIZE = 10;
        IntStream.range(0, SIZE).mapToObj(n -> "https://www.naver.com/" + n)
                .forEach(url -> urlService.createShortURL(url));

        //when
        ResultActions when = mvc.perform(get("/shorts"));

        //then
        when.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionTime").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].path").exists())
                .andExpect(jsonPath("$.data[0].requestCount").exists())
                .andExpect(jsonPath("$.data[0].url").exists())
        ;
    }

    @Transactional
    @DisplayName("하나의 ShortURL에 담긴 로그정보를 모두 조회한다.")
    @Test
    public void fetchShortURLWithLogsByPathTest() throws Exception {
        //given
        int LOG_SIZE = 10;
        ShortURL shortURL = urlService.createShortURL("https://www.naver.com");
        IntStream.range(0, LOG_SIZE).mapToObj(n -> "127.0.0." + n).forEach(ip ->
                urlService.saveLog(shortURL, ClientInfo.builder()
                        .ip(ip)
                        .referer("https://google.com")
                        .build()));

        //when
        ResultActions when = mvc.perform(get("/shorts/" + shortURL.getPath() + "/logs"));

        //then
        when.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionTime").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.path").exists())
                .andExpect(jsonPath("$.data.requestCount").exists())
                .andExpect(jsonPath("$.data.requestCount").value(LOG_SIZE))
                .andExpect(jsonPath("$.data.url").exists())
                .andExpect(jsonPath("$.data.logs").exists())
                .andExpect(jsonPath("$.data.logs").isArray())
                .andExpect(jsonPath("$.data.logs[0].ip").exists())
                .andExpect(jsonPath("$.data.logs[0].referer").exists())
        ;
    }

    @DisplayName("없는 주소를 요청하면 404를 반환한다.")
    @Test
    public void redirect_not_exists() throws Exception {
        //given
        String path = "UNKNOWN";

        //when
        ResultActions when = mvc.perform(get("/" + path));

        //then
        when.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.notFoundResource").value(path));
    }

    @DisplayName("없는 주소의 로그를 조회하려고 하면 404를 반환한다")
    @Test
    public void fetchShortURLWithLogsByPath_not_exists() throws Exception {
        //given
        String path = "NOT_EXISTS";

        //when
        ResultActions when = mvc.perform(get("/shorts/" + path + "/logs"));

        //then
        when.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.notFoundResource").value(path));
    }

}