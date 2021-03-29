package foo.study.url.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SHORTEN_URL")
@Entity
public class ShortenURL {

    @Id
    private String id;

    private String url;

    @Builder
    public ShortenURL(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
