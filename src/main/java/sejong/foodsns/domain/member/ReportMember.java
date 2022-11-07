package sejong.foodsns.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReportMember extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    private BlackList blackList;

    @Column(name = "report_count")
    private int reportCount;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comment;

    @OneToMany
    @JoinColumn(name = "reply_id")
    private List<Reply> replies;

    @Builder
    public ReportMember(BlackList blackList, int reportCount, List<Comment> comment, List<Reply> replies) {
        this.blackList = blackList;
        this.reportCount = reportCount;
        this.comment = comment;
        this.replies = replies;
    }

    // 비즈니스 로직

}
