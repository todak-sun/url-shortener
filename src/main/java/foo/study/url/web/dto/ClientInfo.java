package foo.study.url.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ClientInfo {
    private String ip;
    private String referer;

    @Builder
    public ClientInfo(String ip, String referer) {
        this.ip = ip;
        this.referer = referer;
    }
}
