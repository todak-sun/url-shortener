package foo.study.url.web.dto;

import foo.study.url.domain.ShortenURL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UrlDto {
    public static class Req {
        @Getter
        public static class Create {
            private String url;
        }
    }

    public static class Res {
        @Getter
        @AllArgsConstructor
        public static class Create {
            private String url;
        }
    }
}
