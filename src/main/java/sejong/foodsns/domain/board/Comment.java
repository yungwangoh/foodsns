package sejong.foodsns.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sejong.foodsns.domain.BaseEntity;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "recommend_count")
    private int recommCount;

    @Column(name = "report_count")
    private int reportCount;

    @OneToMany(mappedBy = "comment")
    @JsonIgnore
    private List<Reply> reply;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(String content, int recommCount, int reportCount, List<Reply> reply, Board board) {
        this.content = content;
        this.recommCount = recommCount;
        this.reportCount = reportCount;
        this.reply = reply;
        this.board = board;
    }
}
