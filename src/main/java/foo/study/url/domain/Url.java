package foo.study.url.domain;


import foo.study.url.annotation.FakeId;

import java.util.Objects;

public class Url {

    @FakeId
    private String id;
    private String url;

    public Url(String url) {
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
        Url url = (Url) o;
        return id.equals(url.id) && this.url.equals(url.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

}
