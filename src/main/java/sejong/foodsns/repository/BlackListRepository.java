package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.member.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
