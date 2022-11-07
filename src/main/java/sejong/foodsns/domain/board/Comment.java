package sejong.foodsns.domain.board;

import lombok.*;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @OneToMany
    @JoinColumn(name = "reply_id")
    private List<Reply> replies;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "recommend_count")
    private int recommCount;

    @Column(name = "report_count")
    private int reportCount;

    @Builder
    public Comment(List<Reply> replies, String content, int recommCount, int reportCount) {
        this.replies = replies;
        this.content = content;
        this.recommCount = recommCount;
        this.reportCount = reportCount;
    }
}
