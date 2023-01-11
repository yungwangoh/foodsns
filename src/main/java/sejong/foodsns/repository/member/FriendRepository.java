package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByMember_Email(String email);
    Optional<Friend> findByMemberId(Long id);
}
