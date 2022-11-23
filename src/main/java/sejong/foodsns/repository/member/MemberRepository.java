package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUsername(String name);
    Optional<Member> findMemberByEmail(String email);
    Boolean existsMemberByUsername(String username);
    Boolean existsMemberByEmail(String email);
}
