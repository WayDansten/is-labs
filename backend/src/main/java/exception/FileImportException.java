package exception;

public class FileImportException extends RuntimeException {
    public FileImportException() {
        super();
    }

    public FileImportException(Exception e) {
        super(e);
    }
}
