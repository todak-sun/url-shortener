package foo.study.url.web.dto;

public class ErrorDto {
    public static class NotFound {
        private Object id;

        public NotFound(Object id) {
            this.id = id;
        }

        public Object getId() {
            return id;
        }
    }
}
