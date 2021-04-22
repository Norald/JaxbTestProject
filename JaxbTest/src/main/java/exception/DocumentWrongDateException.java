package exception;

public class DocumentWrongDateException extends RuntimeException {
    public DocumentWrongDateException(String message) {
        super(message);
    }
}
