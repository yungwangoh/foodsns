package sejong.foodsns.repository.querydsl.board;

import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;

import java.util.List;

public interface BoardQueryRepository {

    List<Board> search(SearchOption searchOption, String content);
    List<Board> searchByHighestRecommendCount();

    List<Board> searchByHighestCommentCount();
}
