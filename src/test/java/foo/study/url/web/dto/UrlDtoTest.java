package foo.study.url.web.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class UrlDtoTest {

    Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("유효한 URL 이라면 에러를 검출하지 않는다.")
    @Test
    public void validate_url() {
        // given
        String[] validUrls = new String[]{
                "https://www.naver.com",
                "https://www.google.com",
                "http://www.naver.com",
                "http://www.google.com",
                "http://www.kakao.com"
        };

        // when
        Arrays.stream(validUrls).map(UrlDto.Req.Create::new)
                .map(url -> validator.validate(url).size())
                .reduce(Integer::sum)
                .ifPresent((total) -> {
                    Assertions.assertThat(total).isEqualTo(0);
                });
    }

    @DisplayName("올바른 URL 아니라면 에러를 검출한다")
    @Test
    public void is_invalid_url() throws Exception {
        //given
        String[] invalidUrls = new String[]{
                "htt://www.naver.com",
                "ftp://foo.bar.com",
                "mqtt://test.bar.com",
                "http/test.bar",
                "https:/test.bar"
        };
        //when

        // when
        Arrays.stream(invalidUrls).map(UrlDto.Req.Create::new)
                .map(url -> validator.validate(url).size())
                .reduce(Integer::sum)
                .ifPresent((total) -> {
                    Assertions.assertThat(total).isEqualTo(invalidUrls.length);
                });

        //then
    }

}