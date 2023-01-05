package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
