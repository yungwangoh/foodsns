package sejong.foodsns.exception.http.board;

public class NoSearchReplyException extends IllegalArgumentException {

    public NoSearchReplyException() {}

    public NoSearchReplyException(String s) {
        super(s);
    }

    public NoSearchReplyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchReplyException(Throwable cause) {
        super(cause);
    }
}
