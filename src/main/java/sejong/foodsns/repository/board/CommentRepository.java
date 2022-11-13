package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.board.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
