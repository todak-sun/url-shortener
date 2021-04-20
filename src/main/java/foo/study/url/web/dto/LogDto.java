package foo.study.url.web.dto;

import foo.study.url.domain.entities.RequestLog;
import lombok.Getter;

import java.time.LocalDateTime;

public class LogDto {
    public static class Res {

        @Getter
        public static class Get {
            private String ip;
            private String referer;
            private LocalDateTime time;

            public Get(RequestLog requestLog) {
                this.ip = requestLog.getIp();
                this.referer = requestLog.getReferer();
                this.time = requestLog.getCreatedDateTime();
            }
        }
    }
}
