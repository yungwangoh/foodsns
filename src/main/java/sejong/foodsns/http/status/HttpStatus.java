package sejong.foodsns.http.status;

public interface HttpStatus {

    // http 200번대
    int OK = 200;
    int CREATED = 201;
    int ACCEPTED = 202;
    int NO_CONTENT = 204;

    // http 300번대
    int MOVED_PERMANENTLY = 301;
    int FOUND = 302;
    int SEE_OTHER = 303;
    int NOT_MODIFIED = 304;
    int TEMPORARY_REDIRECT = 307;
    int PERMANENT_REDIRECT = 308;

    // http 400번대
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;

    // http 500번대
    int INTERNAL_SERVER_ERROR = 500;
    int SERVICE_UNAVAILABLE = 503;
}
