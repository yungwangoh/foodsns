package sejong.foodsns.exception.http.board;

public class NoSearchCommentException extends IllegalArgumentException {

    public NoSearchCommentException() {}

    public NoSearchCommentException(String s) {
        super(s);
    }

    public NoSearchCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchCommentException(Throwable cause) {
        super(cause);
    }
}
