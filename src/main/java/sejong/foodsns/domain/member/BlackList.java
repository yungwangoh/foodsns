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

    @Column(name = "penalty_reason")
    private String reason;

    @OneToOne
    @JoinColumn(name = "report_id")
    private ReportMember reportMember;

    @OneToMany
    @JoinColumn(name = "board_id")
    private List<Board> boards;

    @Builder
    public BlackList(String reason) {
        this.reason = reason;
    }

    // 비즈니스 로직 -> 블랙리스트 회원 추가.

    /**
     * @param reportMember
     */
    public void blackListMember(ReportMember reportMember) {
        int penalty = reportMember.getMember().getPenalty();
        if(penalty >= MemberNumberOfCount.penalty) {
            this.reportMember = reportMember;
            blackListProcess(reportMember);
        }
    }

    /**
     * @param reportMember
     */
    // 블랙 리스트에 오른 사람은 게시물 + 댓글 + 대댓글 삭제
    private void blackListProcess(ReportMember reportMember) {
        List<Board> boards = getBoards();
        boards.clear();
    }

    private void blackListMemberDelete(ReportMember reportMember) {
        Member member = reportMember.getMember();

    }
}
