package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
