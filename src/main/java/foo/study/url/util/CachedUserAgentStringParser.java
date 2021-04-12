package foo.study.url.util;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.stereotype.Component;

@Component
public class CachedUserAgentStringParser implements UserAgentStringParser {

    private final UserAgentStringParser parser = UADetectorServiceFactory.getCachingAndUpdatingParser();

    @Override
    public String getDataVersion() {
        return parser.getDataVersion();
    }

    @Override
    public ReadableUserAgent parse(String userAgent) {
        ReadableUserAgent result = parser.parse(userAgent);
        return result;
    }

    @Override
    public void shutdown() {
        parser.shutdown();
    }
}
