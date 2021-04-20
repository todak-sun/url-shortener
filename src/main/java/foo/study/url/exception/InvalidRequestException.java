package foo.study.url.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

public class InvalidRequestException extends RuntimeException {

    @Getter
    private List<FieldError> fieldErrors;

    public InvalidRequestException(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
