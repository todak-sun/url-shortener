package foo.study.url.util;

import foo.study.url.ifs.PathGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Deprecated
public class HashAndEncode64PathGenerator implements PathGenerator {

    @Override
    public String generate(String origin) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(origin.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unknown Error");
        }
        return encodeBase65(md.digest());
    }

    private String encodeBase65(byte[] source) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encoder.encode(source)).substring(0, 8);
    }
}
