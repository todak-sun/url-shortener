package foo.study.url.web.dto;

public class UrlDto {
    public static class Req {
        public static class Create{
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
        public static class Create{

            private String shortenURl;

            public Create(String shortenURl) {
                this.shortenURl = shortenURl;
            }

            public String getShortenURl() {
                return shortenURl;
            }
        }
    }
}
