package foo.study.url.exception;

public class NotFoundException extends RuntimeException {


    private Object id;

    public NotFoundException(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

}
