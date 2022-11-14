package sejong.foodsns.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.FoodTag;

@Repository
public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {
}
