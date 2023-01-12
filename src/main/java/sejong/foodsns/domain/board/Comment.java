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
    @Lob // 문자열 길이 varchar(255) 이상 저장가능
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
        this.recommCount = recommCount; // 추천수
        this.reportCount = reportCount; // 신고수
        this.reply = reply; // 대댓글
        this.board = board; // 댓글을 달 게시물
    }

    // 비즈니스 로직
    public void setBoard(Board board) {
        board.getComments().add(this);
    }

    /**
     * 댓글 내용 수정
     * @param content
     * @return
     */
    public Comment commentContentUpdate(String content) {
        this.content = content;
        return this;
    }
}
