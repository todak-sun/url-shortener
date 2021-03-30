package foo.study.url.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UrlDto {
    public static class Req {

        @NoArgsConstructor
        @Getter
        @Setter
        public static class Create {
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
    }
}
