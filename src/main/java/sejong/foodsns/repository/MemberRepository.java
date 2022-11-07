package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
