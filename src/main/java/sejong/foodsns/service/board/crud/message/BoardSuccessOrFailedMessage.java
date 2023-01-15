package sejong.foodsns.service.board.crud.message;


public interface BoardSuccessOrFailedMessage {

    // 수정 관련
    String BOARD_TITLE_UPDATE_SUCCESS = "제목 수정 성공";
    String USER_CREATE_FAILED = "회원 가입에 실패하였습니다.";

    // 삭제 관련
    String BOARD_DELETE_SUCCESS = "정상적으로 게시물이 삭제되었습니다.";
}
