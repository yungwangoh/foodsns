package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
