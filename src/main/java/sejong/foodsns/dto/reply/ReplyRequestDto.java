package sejong.foodsns.dto.reply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReplyRequestDto {

    private Long id;
    @Schema(description = "대댓글 내용", example = "댓글 믿고 레시피 감사인사 남겨요.")
    private String content;
    @Schema(description = "댓글 id")
    private Long commentId;

    @Schema(description = "닉네임 (유저 이름)")
    private String username;

    @Builder
    public ReplyRequestDto(String content, Long commentId, String username) {
        this.content = content;
        this.commentId = commentId;
        this.username = username;
    }
}
