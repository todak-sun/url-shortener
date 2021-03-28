package foo.study.url.web.dto;

import foo.study.url.domain.ShortenURL;

public class UrlDto {
    public static class Req {
        public static class Create {
            private String url;

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }
    }

    public static class Res {
        public static class Create {
            private String url;

            public Create(ShortenURL shortenURL) {
                this.url = shortenURL.getUrl();
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
