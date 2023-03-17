package sejong.foodsns.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.BaseEntity;
import sejong.foodsns.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reply extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "recommend_count")
    private int recommCount;

    @Column(name = "report_count")
    private int reportCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reply(String content, int recommCount, int reportCount, Comment comment, Member member) {
        this.content = content;
        this.recommCount = recommCount;
        this.reportCount = reportCount;
        this.comment = comment;
        this.member = member;
    }

    /**
     * 대댓글 내용 수정
     * @param content
     * @return
     */
    public Reply replyContentUpdate(String content) {
        this.content = content;
        return this;
    }
}
