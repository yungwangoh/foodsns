package sejong.foodsns.exception.http;

/**
 * 게시물과 회원 둘 다 사용하는 클래스
 */
public class DuplicatedException extends IllegalArgumentException {

    public DuplicatedException() {}

    public DuplicatedException(String s) {
        super(s);
    }

    public DuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedException(Throwable cause) {
        super(cause);
    }
}
