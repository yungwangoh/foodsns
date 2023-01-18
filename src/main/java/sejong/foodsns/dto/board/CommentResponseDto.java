package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    @NotBlank
    private String content;

    private int recommCount;

    private int reportCount;

    private List<Reply> reply;

    private Board board;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.recommCount = comment.getRecommCount();
        this.reportCount = comment.getReportCount();
        this.reply = comment.getReply();
        this.board = comment.getBoard();
    }
}
