package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Recommend;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
