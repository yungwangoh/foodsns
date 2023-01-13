package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :username")
    Optional<Member> findByUsername(@Param("username") String username);
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);
    Boolean existsMemberByUsername(String username);
    Boolean existsMemberByEmail(String email);
}
