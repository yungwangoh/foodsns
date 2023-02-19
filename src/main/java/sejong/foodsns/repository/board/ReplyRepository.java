package sejong.foodsns.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query(value = "select r from Reply r join fetch r.comment c join fetch c.board b join fetch b.member m where m.username = :username")
    List<Reply> findRepliesByUsername(@Param("username") String username);

    @Query(value = "select r from Reply r join fetch r.comment c join fetch c.board b where b.title =:title")
    List<Reply> findRepliesByBoardTitle(@Param("title") String title);

    @Query(value = "select r from Reply r join fetch r.comment c join fetch c.board b where b.title = :title and r.content like %:content%")
    Optional<Reply> findByBoardTitleAndContainingContent(@Param("title") String boardTitle, @Param("content") String content);

    List<Reply> findByContentLike(String content);

    Optional<Reply> findReplyById(Long id);

    Boolean existsReplyByContentLike(String content);
}
