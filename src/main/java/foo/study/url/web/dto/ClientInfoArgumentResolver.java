package foo.study.url.web.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClientInfoArgumentResolver implements HandlerMethodArgumentResolver {

//    private final CachedUserAgentStringParser cachedUserAgentStringParser;

    private final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA",
            "REMOTE_ADDR"
    };

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(ClientInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

//        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
//        ReadableUserAgent parse = cachedUserAgentStringParser.parse(userAgent);
//
//        log.info("origin user-agent : {}", userAgent);
//
//        log.info("name : {}", parse.getName());
//        log.info("os : {}", parse.getOperatingSystem());
//        log.info("category name : {}", parse.getDeviceCategory().getCategory().getName());
//        log.info("versionNumber : {}", parse.getVersionNumber());

        return ClientInfo.builder()
                .ip(getIpAddress(request))
                .referer(getReferer(request))
                .build();
    }


    private String getReferer(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }

    private String getIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
