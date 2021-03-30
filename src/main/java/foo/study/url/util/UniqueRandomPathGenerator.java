package foo.study.url.util;

import foo.study.url.domain.repositories.ShortURLRepository;
import foo.study.url.ifs.RandomPathGenerator;
import foo.study.url.ifs.Uniqueable;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
public class UniqueRandomPathGenerator implements RandomPathGenerator, Uniqueable<String> {

    private final ShortURLRepository shortURLRepository;

    @Override
    public String generate() {
        String path = generatePath();
        while (!isUnique(path)) {
            path = generatePath();
        }
        return path;
    }

    private String generatePath() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(generateRandomValue().getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unknown Error...");
        }
        return encodeBase64(md.digest());
    }

    private String generateRandomValue() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String encodeBase64(byte[] source) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encoder.encode(source)).substring(0, 8);
    }

    @Override
    public boolean isUnique(String value) {
        return !shortURLRepository.existsByPath(value);
    }
}
