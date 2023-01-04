package sejong.foodsns.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static sejong.foodsns.domain.member.MemberNumberOfCount.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReportMember extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportMember(Member member) {
        this.member = member;
    }

    /**
     * 블랙리스트 회원 패널티 개수 세기
     * @param member
     * @return
     */
    public int blackListPenaltyCount(Member member) {

        return penaltyCalculate(member);
    }

    /**
     * 회원의 패널티 수 증가. -> 회원의 패널티 수는 신고 횟수의 1 / 10 -> 3개이면 블랙리스트 추가.
     * @param member
     * @return 회원 패널티 개수
     */
    private int penaltyCalculate(Member member) {
        if(penaltyFirst(member)) {
            member.penaltyCount();
        } else if (penaltySecond(member)) {
            member.penaltyCount();
        } else if (penaltyThird(member)) {
            member.penaltyCount();
        }

        return member.getPenalty();
    }

    /**
     * 신고 횟수가 30개 이상이면서, 패널티 개수가 2개면 패널티 수 증가.
     * @param member
     * @return
     */
    private boolean penaltyThird(Member member) {
        return member.getReportCount() >= numOfReportThird && member.getPenalty() >= 2;
    }

    /**
     * 신고 횟수가 20개 이상이면서, 패널티 개수가 1개면 패널티 수 증가.
     * @param member
     * @return
     */
    private boolean penaltySecond(Member member) {
        return (member.getReportCount() >= numOfReportSecond && member.getReportCount() < numOfReportThird) && member.getPenalty() == 1;
    }

    /**
     * 신고 횟수가 10개 이상이면서, 패널티 개수가 0개면 패널티 수 증가.
     * @param member
     * @return
     */
    private boolean penaltyFirst(Member member) {
        return (member.getReportCount() >= numOfReportFirst && member.getReportCount() < numOfReportSecond) && member.getPenalty() == 0;
    }
}
