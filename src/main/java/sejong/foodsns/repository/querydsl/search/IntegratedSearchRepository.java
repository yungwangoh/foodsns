package sejong.foodsns.repository.querydsl.search;

import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;

import java.util.List;
import java.util.Optional;

public interface IntegratedSearchRepository {

    List<Board> boardIntegratedSearch(String content);
    List<Comment> commentIntegratedSearch(String content);
    List<Reply> replyIntegratedSearch(String content);
}
