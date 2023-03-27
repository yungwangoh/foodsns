package sejong.foodsns.dto.comment;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private int recommCount;
    private int reportCount;
    private MemberResponseDto memberResponseDto;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.recommCount = comment.getRecommCount();
        this.reportCount = comment.getReportCount();
        this.memberResponseDto = new MemberResponseDto(comment.getMember());
    }
}
