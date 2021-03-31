package foo.study.url.web;

import foo.study.url.domain.entities.ShortURL;
import foo.study.url.service.UrlService;
import foo.study.url.web.dto.ClientInfo;
import foo.study.url.web.dto.Response;
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

    @GetMapping("/{path}") //TODO: 추후 모듈 분리로 이동될 핸들러
    public ResponseEntity<?> redirect(@PathVariable String path, ClientInfo clientInfo) throws URISyntaxException {
        log.info("client info : {}", clientInfo);
        ShortURL shortURL = urlService.fetchShortURLWithOriginURL(path);
        String redirectURL = shortURL.getOriginURL().getUrl();

        urlService.saveLog(shortURL, clientInfo);

        URI redirectUri = new URI(redirectURL);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(httpHeaders).build();
    }

    @GetMapping("/shorts")
    public ResponseEntity<?> fetchAllPaths() {

        List<UrlDto.Res.GetShortURL> body = urlService.fetchAllShortenURLs()
                .stream().map(UrlDto.Res.GetShortURL::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(body, "OK"));
    }

    @GetMapping("/shorts/{path}/logs")
    public ResponseEntity<?> fetchShortURLWithLogsByPath(@PathVariable String path) {
        ShortURL shortURL = urlService.fetchShortURLWithLogsByPath(path);
        UrlDto.Res.GetShortURLWithLog data = new UrlDto.Res.GetShortURLWithLog(shortURL);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(data, "OK"));
    }

    @GetMapping
    public ResponseEntity<?> index() {
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }

}
