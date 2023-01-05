package sejong.foodsns.service.member.login;

public interface MemberLoginAndLogoutMessage {

    // login message
    String LOGIN_SUCCESS = "로그인에 성공하였습니다.";
    String LOGIN_FAIL = "로그인에 실패하셨습니다.";

    // logout message
    String LOGOUT_SUCCESS = "로그아웃에 성공하였습니다.";
    String LOGOUT_FAIL = "로그아웃에 실패하였습니다.";
}
