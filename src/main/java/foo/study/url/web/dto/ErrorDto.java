package foo.study.url.web.dto;

public class ErrorDto {
    public static class NotFound {
        private final Object notFoundResource;

        public NotFound(Object notFoundResource) {
            this.notFoundResource = notFoundResource;
        }

        public Object getNotFoundResource() {
            return notFoundResource;
        }
    }
}
