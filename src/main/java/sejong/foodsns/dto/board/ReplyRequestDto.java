package sejong.foodsns.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReplyRequestDto {

    @Schema(description = "대댓글 내용", example = "댓글 믿고 레시피 감사인사 남겨요.")
    private String content;
    @Schema(description = "대댓글 달린 댓글", example = "이거 레시피 좋네요.")
    private CommentRequestDto commentRequestDto;
    private BoardRequestDto boardRequestDto;

    @Builder
    public ReplyRequestDto(String content, BoardRequestDto boardRequestDto, CommentRequestDto commentRequestDto) {
        this.content = content;
        this.commentRequestDto = commentRequestDto;
        this.boardRequestDto = boardRequestDto;
    }

    public Reply toEntity() {
        return Reply.builder()
                .content(content)
                .comment(commentRequestDto.toEntity())
                .reportCount(0)
                .recommCount(0)
                .build();
    }
}
