package foo.study.url.service;

import foo.study.url.domain.entities.OriginURL;
import foo.study.url.domain.entities.RequestLog;
import foo.study.url.domain.entities.ShortURL;
import foo.study.url.domain.repositories.OriginURLRepository;
import foo.study.url.domain.repositories.RequestLogRepository;
import foo.study.url.domain.repositories.ShortURLRepository;
import foo.study.url.util.UniqueRandomPathGenerator;
import foo.study.url.web.dto.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public String getOriginURLByPath(String path, ClientInfo clientInfo) {
        ShortURL shortURL = shortURLRepository.findByPath(path)
                .orElseThrow(() -> {
                    //TODO: 적절한 에러 생성 후 에러코드 반환할 것.
                    throw new IllegalArgumentException();
                });

        RequestLog requestLog = RequestLog.builder()
                .ip(clientInfo.getIp())
                .referer(clientInfo.getReferer())
                .build();

        requestLog.loggedFrom(shortURL);
        requestLogRepository.save(requestLog);


        return shortURL.getOriginURL().getUrl();
    }


}
