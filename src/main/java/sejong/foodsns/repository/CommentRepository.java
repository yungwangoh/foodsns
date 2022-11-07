package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.board.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
