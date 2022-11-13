package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.member.ReportMember;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {
}
