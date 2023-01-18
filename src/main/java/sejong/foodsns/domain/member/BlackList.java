package sejong.foodsns.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Board;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlackList extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "black_list_id")
    private Long id;

    // 블랙리스트인 사유
    @Column(name = "penalty_reason")
    private String reason;

    // 신고 당한 회원
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "report_id")
    private ReportMember reportMember;

    @Builder
    public BlackList(String reason, ReportMember reportMember) {
        this.reportMember = reportMember;
        this.reason = reason;
    }

    // 비즈니스 로직 -> 블랙리스트 회원 추가.

    /**
     * 유저 패널티가 3개 이상이면 유저의 관한 모든 정보 삭제, 회원 탈퇴
     * @param reportMember
     * @return
     */
    public void blackListMember(ReportMember reportMember) {
        int penalty = reportMember.getMember().getPenalty();
        if(penalty >= MemberNumberOfCount.penalty) {
            blackListProcess(reportMember);
        }
    }

    /**
     * 블랙 리스트에 오른 사람은 게시물 + 댓글 + 대댓글 삭제
     * @param reportMember
     */
    private void blackListProcess(ReportMember reportMember) {
        List<Board> boards = reportMember.getMember().getBoards();
        boards.clear();
    }
}
