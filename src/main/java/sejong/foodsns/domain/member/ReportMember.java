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
    public static int blackListPenaltyCount(Member member) {

        return penaltyCalculate(member);
    }

    /**
     * 회원의 패널티 수 증가. -> 회원의 패널티 수는 신고 횟수의 1 / 10 -> 3개이면 블랙리스트 추가.
     * @param member
     * @return 회원 패널티 개수
     */
    private static int penaltyCalculate(Member member) {
        if(penaltyFirst(member)) {
            member.setPenalty(1);
        } else if (penaltySecond(member)) {
            member.setPenalty(2);
        } else if (penaltyThird(member)) {
            member.setPenalty(3);
        }

        return member.getPenalty();
    }

    /**
     * 신고 횟수가 30개 이상이면 penalty 3
     * @param member
     * @return
     */
    private static boolean penaltyThird(Member member) {
        return member.getReportCount() >= numOfReportThird;
    }

    /**
     * 신고 횟수가 20개 이상이면 penalty 2
     * @param member
     * @return
     */
    private static boolean penaltySecond(Member member) {
        return (member.getReportCount() >= numOfReportSecond && member.getReportCount() < numOfReportThird);
    }

    /**
     * 신고 횟수가 10개 이상이면 penalty 1
     * @param member
     * @return
     */
    private static boolean penaltyFirst(Member member) {
        return (member.getReportCount() >= numOfReportFirst && member.getReportCount() < numOfReportSecond);
    }
}
