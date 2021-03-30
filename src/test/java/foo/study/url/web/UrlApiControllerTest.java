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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
class UrlApiControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private UrlService urlService;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

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
                .andExpect(header().string(HttpHeaders.LOCATION, url))
        ;
    }


    @DisplayName("줄인 URL의 로그를 조회한다.")
    @Test
    public void fetch_logs_by_path_test() throws Exception {
        //given
        String url = "https://www.naver.com";
        ShortURL shortURL = urlService.createShortURL(url);
        String path = shortURL.getPath();

        ClientInfo clientInfo = ClientInfo.builder()
                .referer("https://google.com")
                .ip("127.0.0.1")
                .build();

        urlService.getOriginURLByPath(path, clientInfo);

        //when
        ResultActions when = mvc.perform(get("/logs/" + path));

        //then
        when.andExpect(status().isOk());

    }


}