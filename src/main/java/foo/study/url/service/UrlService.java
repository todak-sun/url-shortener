package foo.study.url.service;

import foo.study.url.domain.entities.OriginURL;
import foo.study.url.domain.entities.RequestLog;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.domain.repositories.OriginURLRepository;
import foo.study.url.domain.repositories.RequestLogRepository;
import foo.study.url.domain.repositories.ShortURLRepository;
import foo.study.url.exception.NotFoundException;
import foo.study.url.util.UniqueRandomPathGenerator;
import foo.study.url.web.dto.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UrlService {

    private final OriginURLRepository originURLRepository;

    private final ShortURLRepository shortURLRepository;

    private final RequestLogRepository requestLogRepository;

    @Transactional
    public ShortURL createShortURL(String url) {
        OriginURL originURL = originURLRepository.findByUrl(url)
                .orElseGet(() -> originURLRepository.save(OriginURL.builder().url(url).build()));

        ShortURL shortURL = ShortURL.builder().build();
        shortURL.generatePath(new UniqueRandomPathGenerator(shortURLRepository));
        shortURL.referTo(originURL);

        return shortURLRepository.save(shortURL);
    }

    @Transactional
    public RequestLog saveLog(ShortURL shortURL, ClientInfo clientInfo) {
        RequestLog requestLog = RequestLog.builder()
                .ip(clientInfo.getIp())
                .referer(clientInfo.getReferer())
                .build();
        requestLog.loggedFrom(shortURL);
        return requestLogRepository.save(requestLog);
    }

    public ShortURL fetchShortURLWithOriginURL(String path) {
        return shortURLRepository.findByPathWithOriginURL(path)
                .orElseThrow(() -> {
                    throw new NotFoundException(path);
                });
    }

    public List<ShortURL> fetchAllShortenURLs() {
        return shortURLRepository.findAllWithOriginURL();
    }

    public ShortURL fetchShortURLWithLogsByPath(String path) {
        return shortURLRepository.findWithRequestLogsByPath(path)
                .orElseThrow(() -> {
                    throw new NotFoundException(path);
                });
    }
}
