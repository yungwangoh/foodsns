package sejong.foodsns.exception.http;

public class NoSearchMemberException extends IllegalArgumentException {

    public NoSearchMemberException() {}

    public NoSearchMemberException(String s) {
        super(s);
    }

    public NoSearchMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchMemberException(Throwable cause) {
        super(cause);
    }
}
