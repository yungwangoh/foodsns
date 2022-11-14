package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.ReportMember;

@Repository
public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {
}
