package foo.study.url.util;

import foo.study.url.ifs.IdGenerator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UUIDGenerator implements IdGenerator {

    @Override
    public String generate(String origin) {
        return String.valueOf(UUID.nameUUIDFromBytes(origin.getBytes(StandardCharsets.UTF_8))).split("-")[0];
    }

    @Override
    public String generate() {
        return String.valueOf(UUID.randomUUID()).split("-")[0];
    }
}
