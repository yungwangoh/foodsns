package sejong.foodsns.service.member.crud;

public interface MemberSuccessOrFailedMessage {

    // 등록 관련
    String USER_CREATE_SUCCESS = "회원 가입에 성공하였습니다.";
    String USER_CREATE_FAILED = "회원 가입에 실패하였습니다.";

    // 수정 관련
    String USERNAME_UPDATE_SUCCESS = "닉네임 수정을 성공하였습니다.";
    String USERNAME_UPDATE_FAILED = "닉네임 수정을 실패하였습니다.";

    // 찾기 관련
    String PASSWORD_SEARCH_SUCCESS = "비밀번호 찾기를 성공하였습니다.";
    String USERINFO_SEARCH_SUCCESS = "회원 정보 찾기를 성공하였습니다.";

    String PASSWORD_SEARCH_FAILED = "비밀번호 찾기를 실패하였습니다.";
    String USERINFO_SEARCH_FAILED = "회원 정보 찾기를 실패하였습니다.";

    // 탈퇴 관련
    String USER_DELETE_SUCCESS = "정상적으로 회원 탈퇴하였습니다.";
    String USER_DELETE_FAILED = "회원 탈퇴에 실패하였습니다.";

    // 친구 관련
    String FRIEND_ADD_SUCCESS = "친구 추가가 완료되었습니다.";
    String FRIEND_ADD_FAILED = "친구 추가에 실패하였습니다.";

    String FRIEND_SEARCH_SUCCESS = "친구 찾기에 성공하였습니다.";
    String FRIEND_SEARCH_FAILED = "친구 찾기에 실패하였습니다.";

    String FRIEND_LIST_SUCCESS = "친구 리스트 조회에 성공하였습니다.";

    String FRIEND_DELETE_SUCCESS = "친구 삭제에 성공하였습니다.";
    String FRIEND_DELETE_FAILED = "친구 삭제에 실패하였습니다.";
}
