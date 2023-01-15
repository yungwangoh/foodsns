package sejong.foodsns.repository.member;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
