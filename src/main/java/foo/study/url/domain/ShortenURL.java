package foo.study.url.domain;


import foo.study.url.annotation.FakeId;

import java.util.Objects;

public class ShortenURL {

    @FakeId
    private String id;
    private String url;

    public ShortenURL(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortenURL shortenURL = (ShortenURL) o;
        return id.equals(shortenURL.id) && this.url.equals(shortenURL.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", url:'" + url + '\'' +
                '}';
    }
}
