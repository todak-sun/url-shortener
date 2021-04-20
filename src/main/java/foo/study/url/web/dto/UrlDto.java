package foo.study.url.web.dto;

import foo.study.url.domain.entities.ShortURL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

public class UrlDto {
    public static class Req {

        @NoArgsConstructor
        @Getter
        @Setter
        public static class Create {
            @URL(message = "유효한 URL이 아닙니다.")
            @NotEmpty(message = "URL에는 빈 값이 올 수 없습니다.")
            private String url;
            public Create(String url) {
                this.url = url;
            }
        }
    }

    public static class Res {
        @Getter
        @AllArgsConstructor
        public static class Create {
            private String path;
        }

        @Getter
        @AllArgsConstructor
        public static class GetShortURL {
            private String path;
            private long requestCount;
            private String url;

            public GetShortURL(ShortURL shortURL) {
                this.path = shortURL.getPath();
                this.requestCount = shortURL.getRequestCount();
                this.url = shortURL.getOriginURL().getUrl();
            }
        }

        @Getter
        @AllArgsConstructor
        public static class GetShortURLWithLog {
            private String path;
            private long requestCount;
            private String url;

            private List<LogDto.Res.Get> logs;

            public GetShortURLWithLog(ShortURL shortURL) {
                this.path = shortURL.getPath();
                this.requestCount = shortURL.getRequestCount();
                this.url = shortURL.getOriginURL().getUrl();

                this.logs = shortURL.getRequestLogs().stream().map(LogDto.Res.Get::new)
                        .collect(Collectors.toList());
            }

        }
    }

}
