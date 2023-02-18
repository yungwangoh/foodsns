package sejong.foodsns.dto.board;

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
public class CommentRequestDto {

    private Long id;

    @NotBlank
    private String content;

    private MemberRequestDto memberRequestDto;

    private int recommCount;

    private int reportCount;

//    private List<Reply> reply;

    private BoardRequestDto boardRequestDto;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .recommCount(recommCount)
                .reportCount(reportCount)
                .board(boardRequestDto.toEntity())
                .build();
    }
}
