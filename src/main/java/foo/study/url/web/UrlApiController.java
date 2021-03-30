package foo.study.url.web;

import foo.study.url.domain.entities.ShortURL;
import foo.study.url.service.UrlService;
import foo.study.url.web.dto.ClientInfo;
import foo.study.url.web.dto.UrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

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
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/{path}")
    public ResponseEntity<?> fetchOne(@PathVariable String path, ClientInfo clientInfo) throws URISyntaxException {
        log.info("client info : {}", clientInfo);
        //TODO: 여기부터 이어서
//        urlService.getOriginURLByPath(path, clientInfo);
//        ShortenURL shortenURL = urlRepository.findById(id).orElseThrow(() -> {
//            throw new NotFoundException(id);
//        });
//
//        URI redirectUri = new URI(shortenURL.getUrl());
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(redirectUri);

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping
    public ResponseEntity<?> index() {
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }


}
