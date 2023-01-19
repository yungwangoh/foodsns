package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

//    Optional<Board> findBoardByUsername(String name); // query dsl
    Optional<Board> findBoardByTitle(String title);
    List<Board> findBoardByMemberRank(MemberRank memberRank); // 랭크별 게시물 조회
    Boolean existsBoardByTitle(String title);
    void deleteBoardByMemberEmailAndMemberPassword(String email, String password);

}
