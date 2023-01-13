package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.member.MemberRank;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //    Optional<Board> findBoardByUsername(String name); // query dsl
    @Query("select c from Comment c join fetch c.board b where b.title = :title and c.content like %:content%")
    Optional<Comment> findByBoardTitleAndContainingContent(@Param("title") String boardTitle, @Param("content") String content);

    List<Comment> findByContentLike(String content);

    Optional<Comment> findCommentById(Long id);

    Boolean existsCommentByContentLike(String content);
}
