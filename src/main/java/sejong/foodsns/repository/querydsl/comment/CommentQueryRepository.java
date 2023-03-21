package sejong.foodsns.repository.querydsl.comment;

import sejong.foodsns.domain.board.Comment;

import java.util.List;

public interface CommentQueryRepository {

    List<Comment> searchComments(String content);
    List<Comment> searchCommentsUserName(String username);
}
