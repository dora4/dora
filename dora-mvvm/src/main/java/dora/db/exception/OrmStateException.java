package dora.db.exception;

public class OrmStateException extends IllegalStateException {

    public OrmStateException() {
    }

    public OrmStateException(String message) {
        super(message);
    }
}
