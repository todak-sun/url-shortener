package foo.study.url.domain;

import foo.study.url.annotation.FakeId;
import foo.study.url.ifs.IdGenerator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class UrlHashMapRepository implements UrlRepository {

    private final HashMap<String, Url> memo;
    private final IdGenerator idGenerator;

    public UrlHashMapRepository(IdGenerator idGenerator) {
        this.memo = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    @Override
    public Optional<Url> findById(String id) {
        return Optional.ofNullable(memo.get(id));
    }

    @Override
    public Url save(Url url) {
        if (url.getId() != null) {
            memo.put(url.getId(), url);
        } else {
            this.save(this.memo, url);
        }
        return url;
    }

    @Override
    public void delete(Url entity) throws IllegalArgumentException {
        if (!existsById(entity.getId())) {
            throw new IllegalArgumentException();
        }
        this.memo.remove(entity.getId());
    }

    @Override
    public void deleteById(String s) {
        if (!existsById(s)) {
            throw new IllegalArgumentException();
        }
        this.memo.remove(s);
    }

    @Override
    public boolean existsById(String s) {
        return this.memo.containsKey(s);
    }

    @Override
    public long count() {
        return this.memo.size();
    }

    private void save(HashMap<String, Url> memo, Url url) {
        Field idField = Arrays.stream(Url.class.getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(an -> an.annotationType() == FakeId.class))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Not Found @FakeId");
                });

        idField.setAccessible(true);
        String id = idGenerator.generate(url.getUrl());
        while (!existsById(id)) {
            try {
                idField.set(url, id);
                memo.put(id, url);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Unknown Exception");
            }
        }
    }
}
