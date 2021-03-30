package foo.study.url.web.dto;

import foo.study.url.domain.entities.RequestLog;
import lombok.Getter;

public class LogDto {
    public static class Res {

        @Getter
        public static class Get {
            private String ip;
            private String referer;

            public Get(RequestLog requestLog) {
                this.ip = requestLog.getIp();
                this.referer = requestLog.getReferer();
            }
        }
    }
}
