package foo.study.url.ifs;

import foo.study.url.util.HashAndEncode64PathGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathGeneratorTest {

    PathGenerator pathGenerator;

    @DisplayName("HashAndEncode64IdGenerator - 같은 URL을 넣었을 때, 같은 값을 리턴하는지 테스트")
    @Test
    public void hashAndEncodeBase64IdGeneratorTest() {
        pathGenerator = new HashAndEncode64PathGenerator();
        String a = pathGenerator.generate("https://www.naver.com");
        assertEquals(a, pathGenerator.generate("https://www.naver.com"));
    }

    @DisplayName("HashAndEncode64IdGenerator - 8개의 문자열이 생성되는지 테스트")
    @Test
    public void length_just_8() {
        pathGenerator = new HashAndEncode64PathGenerator();
        assertEquals(8, pathGenerator.generate("https://www.google.com").length());
    }

}