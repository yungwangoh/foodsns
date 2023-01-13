package sejong.foodsns.exception.http.board;

public class NoSearchBoardException extends IllegalArgumentException {

    public NoSearchBoardException() {}

    public NoSearchBoardException(String s) {
        super(s);
    }

    public NoSearchBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchBoardException(Throwable cause) {
        super(cause);
    }
}
