package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.BlackList;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    @Query("select b from BlackList b join fetch b.member m where m.id = :memberId")
    Optional<BlackList> findBlackListById(@Param("memberId") Long id);
}
