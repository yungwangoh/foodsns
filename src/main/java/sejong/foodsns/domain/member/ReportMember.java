package sejong.foodsns.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;
import static sejong.foodsns.domain.member.MemberNumberOfCount.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReportMember extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "black_list_id")
    @JsonIgnore
    private BlackList blackList;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportMember(BlackList blackList) {
        this.blackList = blackList;
    }

    // 회원의 신고 수가 10개 이상이면 회원 신고 리포트에 저장.
    public void memberReport(Member member) {
        Long reportCount = member.getReportCount();

        reportSave(member, reportCount);
    }

    public int blackListPenaltyCount(Member member) {

        return penaltyCalculate(member);
    }

    private void reportSave(Member member, Long reportCount) {
        if(reportCount >= numOfReportFirst) { // 신고 수가 10개 이상
            this.member = member;
        }
    }

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

    private boolean penaltyThird(Member member) {
        return member.getReportCount() >= numOfReportThird && member.getPenalty() >= 2;
    }

    private boolean penaltySecond(Member member) {
        return (member.getReportCount() >= numOfReportSecond && member.getReportCount() < numOfReportThird) && member.getPenalty() == 1;
    }

    private boolean penaltyFirst(Member member) {
        return (member.getReportCount() >= numOfReportFirst && member.getReportCount() < numOfReportSecond) && member.getPenalty() == 0;
    }
}
