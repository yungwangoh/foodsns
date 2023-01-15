package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f join fetch f.member m where m.email = :email")
    Optional<Friend> findByMember_Email(@Param("email") String email);
    @Query("select f from Friend f join fetch f.member m where m.id = :memberId")
    Optional<Friend> findByMemberId(@Param("memberId") Long id);
    @Query("select m from Member m join fetch m.friends where m.id = :memberId")
    List<Friend> findFriendsByMemberId(@Param("memberId") Long id);
}
