package exception;

public class WrongDocumentException extends RuntimeException {
    public WrongDocumentException(String message) {
        super(message);
    }
}
