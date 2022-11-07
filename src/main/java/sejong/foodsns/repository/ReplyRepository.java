package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.board.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
