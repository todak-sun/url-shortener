package foo.study.url.web.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Response<T> {
    private T data;

    private LocalDateTime transactionTime;
    private String message;

    public Response(final T data, final String message) {
        this.data = data;
        this.message = message;
        this.transactionTime = LocalDateTime.now();
    }
}
