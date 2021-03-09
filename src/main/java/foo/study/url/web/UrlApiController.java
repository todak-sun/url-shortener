package foo.study.url.web;

import foo.study.url.domain.Url;
import foo.study.url.domain.UrlRepository;
import foo.study.url.exception.NotFoundException;
import foo.study.url.web.dto.UrlDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RequestMapping("/")
@RestController
public class UrlApiController {

    private final UrlRepository urlRepository;

    public UrlApiController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UrlDto.Req.Create req) {
        String id = urlRepository.save(new Url(req.getUrl())).getId();
        UrlDto.Res.Create res = new UrlDto.Res.Create(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchOne(@PathVariable String id) throws URISyntaxException {
        Url url = urlRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(id);
        });

        URI redirectUri = new URI(url.getUrl());
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
