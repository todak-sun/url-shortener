package foo.study.url.web.dto;

import lombok.Getter;
import org.springframework.validation.FieldError;

public class ErrorDto {

    @Getter
    public static class NotFound {
        private final Object notFoundResource;

        public NotFound(Object notFoundResource) {
            this.notFoundResource = notFoundResource;
        }
    }


    @Getter
    public static class InvalidRequestException {
        private String invalidField;
        private Object rejectedValue;
        private String message;

        public InvalidRequestException(FieldError error) {
            this.invalidField = error.getField();
            this.rejectedValue = error.getRejectedValue();
            this.message = error.getDefaultMessage();
        }
    }

}
