package foo.study.url.ifs;

import foo.study.url.util.HashAndEncode64IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    IdGenerator idGenerator;

    @DisplayName("HashAndEncode64IdGenerator - 같은 URL을 넣었을 때, 같은 값을 리턴하는지 테스트")
    @Test
    public void hashAndEncodeBase64IdGeneratorTest() {
        idGenerator = new HashAndEncode64IdGenerator();
        String a = idGenerator.generate("https://www.naver.com");
        assertEquals(a, idGenerator.generate("https://www.naver.com"));
    }

    @DisplayName("HashAndEncode64IdGenerator - 8개의 문자열이 생성되는지 테스트")
    @Test
    public void length_just_8() {
        idGenerator = new HashAndEncode64IdGenerator();
        assertEquals(8, idGenerator.generate("https://www.google.com").length());
    }

}