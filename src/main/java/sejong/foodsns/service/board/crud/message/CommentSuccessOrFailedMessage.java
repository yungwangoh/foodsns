package sejong.foodsns.service.board.crud.message;


public interface CommentSuccessOrFailedMessage {

    // 수정 관련
    String COMMENT_CONTENT_UPDATE_SUCCESS = "댓글 수정 성공";

    // 삭제 관련
    String COMMENT_DELETE_SUCCESS = "정상적으로 댓글이 삭제되었습니다.";
}
