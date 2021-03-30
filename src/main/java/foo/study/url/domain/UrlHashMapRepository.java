package foo.study.url.domain;

import foo.study.url.annotation.FakeId;
import foo.study.url.ifs.PathGenerator;

import java.lang.reflect.Field;
import java.util.*;

@Deprecated
public class UrlHashMapRepository implements UrlRepository {

    private final HashMap<String, ShortenURL> idMemo;
    private final HashMap<String, ShortenURL> urlMemo;

    private final PathGenerator pathGenerator;

    public UrlHashMapRepository(PathGenerator pathGenerator) {
        this.idMemo = new HashMap<>();
        this.urlMemo = new HashMap<>();
        this.pathGenerator = pathGenerator;
    }

    @Override
    public Optional<ShortenURL> findById(String id) {
        return Optional.ofNullable(idMemo.get(id));
    }

    @Override
    public List<ShortenURL> findAll() {
        return new ArrayList<>(this.idMemo.values());
    }

    @Override
    public ShortenURL save(ShortenURL shortenURL) {
        if (existsByUrl(shortenURL.getUrl())) { // 같은 URL 이 존재하면 그대로 반환.
            return urlMemo.get(shortenURL.getUrl());
        } else { // 같은 URL 이 존재하지 않으면서
            if (shortenURL.getId() == null) { // 아이디도 없다면 새로 저장
                this.persist(shortenURL);
            }
        }
        return shortenURL;
    }

    @Override
    public void delete(ShortenURL entity) throws IllegalArgumentException {
        if (!existsById(entity.getId())) {
            throw new IllegalArgumentException();
        }
        this.urlMemo.remove(entity.getUrl());
        this.idMemo.remove(entity.getId());
    }

    @Override
    public void deleteById(String s) {
        if (!existsById(s)) {
            throw new IllegalArgumentException();
        }
        ShortenURL shortenURL = idMemo.get(s);
        this.urlMemo.remove(shortenURL.getUrl());
        this.idMemo.remove(s);
    }

    @Override
    public void deleteAll() {
        this.idMemo.clear();
        this.urlMemo.clear();
    }

    @Override
    public boolean existsById(String s) {
        return this.idMemo.containsKey(s);
    }

    @Override
    public boolean existsByUrl(String url) {
        return this.urlMemo.containsKey(url);
    }

    @Override
    public long count() {
        return this.idMemo.size();
    }

    private void persist(ShortenURL shortenURL) {
        Field idField = Arrays.stream(ShortenURL.class.getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(an -> an.annotationType() == FakeId.class))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Not Found @FakeId");
                });

        idField.setAccessible(true);
        String id = pathGenerator.generate(shortenURL.getUrl());
        while (!existsById(id)) {
            try {
                idField.set(shortenURL, id);
                idMemo.put(id, shortenURL);
                urlMemo.put(shortenURL.getUrl(), shortenURL);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Unknown Exception");
            }
        }
    }


}
