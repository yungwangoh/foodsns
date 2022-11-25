package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
