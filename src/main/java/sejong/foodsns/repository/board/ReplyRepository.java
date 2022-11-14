package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
