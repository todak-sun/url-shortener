package foo.study.url.web.dto;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class UrlDtoTest {

    @Test
    public void validate_url(){
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UrlDto.Req.Create create = new UrlDto.Req.Create("https://www.naver.com");
    }

}