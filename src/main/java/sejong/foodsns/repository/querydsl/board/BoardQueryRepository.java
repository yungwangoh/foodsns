package sejong.foodsns.repository.querydsl.board;

import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;

import java.util.List;

public interface BoardQueryRepository {

    List<Board> search(SearchOption searchOption, String content);
    void recommendUp(Board b);
    void recommendDown(Board b);
    boolean checkRecommendMemberAndBoard(Member m, Board b);
    List<Board> searchByHighestRecommendCount();
    List<Board> searchByHighestCommentCount();
}
