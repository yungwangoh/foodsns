package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByUsername(String name);
}
