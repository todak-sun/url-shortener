package foo.study.url.web;

import foo.study.url.domain.entities.RequestLog;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.service.UrlService;
import foo.study.url.web.dto.ClientInfo;
import foo.study.url.web.dto.LogDto;
import foo.study.url.web.dto.UrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UrlApiController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UrlDto.Req.Create req) {
        ShortURL shortURL = urlService.createShortURL(req.getUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UrlDto.Res.Create(shortURL.getPath()));

    }

    @GetMapping("/logs")
    public ResponseEntity<?> fetchAllPaths() {
        //TODO: 서버에 생성된 줄인 URL을 모두 출력하기 구현
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/logs/{path}")
    public ResponseEntity<?> fetchAllLogsByPath(@PathVariable String path) {
        List<RequestLog> requestLogs = urlService.fetchRequestLogByPath(path);
        return ResponseEntity.status(HttpStatus.OK)
                .body(requestLogs.stream().map(LogDto.Res.Get::new).collect(Collectors.toList()));
    }

    @GetMapping("/{path}")
    public ResponseEntity<?> redirect(@PathVariable String path, ClientInfo clientInfo) throws URISyntaxException {
        log.info("client info : {}", clientInfo);
        String redirectURL = urlService.getOriginURLByPath(path, clientInfo);

        URI redirectUri = new URI(redirectURL);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(httpHeaders).build();
    }

    @GetMapping
    public ResponseEntity<?> index() {
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }


}
