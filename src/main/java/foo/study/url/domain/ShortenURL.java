package foo.study.url.domain;


import foo.study.url.annotation.FakeId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Deprecated
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenURL {

    @FakeId
    private String id;

    private String url;

    @Builder
    public ShortenURL(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
