package foo.study.url.web;

import foo.study.url.domain.ShortenURL;
import foo.study.url.domain.UrlRepository;
import foo.study.url.exception.NotFoundException;
import foo.study.url.web.dto.UrlDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/")
@RestController
public class UrlApiController {

    private final UrlRepository urlRepository;

    public UrlApiController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UrlDto.Req.Create req) {
        ShortenURL save = urlRepository.save(new ShortenURL(req.getUrl()));
        UrlDto.Res.Create res = new UrlDto.Res.Create(save);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);

    }

    @GetMapping("/logs")
    public ResponseEntity<?> fetchAll() {
        List<UrlDto.Res.Create> res = urlRepository.findAll().stream().map(UrlDto.Res.Create::new).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchOne(@PathVariable String id) throws URISyntaxException {
        ShortenURL shortenURL = urlRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(id);
        });

        URI redirectUri = new URI(shortenURL.getUrl());
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
