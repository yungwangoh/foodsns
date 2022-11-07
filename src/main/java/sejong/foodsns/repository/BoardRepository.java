package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
