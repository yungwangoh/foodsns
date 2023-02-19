package sejong.foodsns.dto.board;

import lombok.*;
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

    private Long id;
    private String content;
    private int recommCount;
    private int reportCount;
    private CommentRequestDto commentRequestDto;

    public Reply toEntity() {
        return Reply.builder()
                .content(content)
                .recommCount(recommCount)
                .reportCount(reportCount)
                .comment(commentRequestDto.toEntity())
                .build();
    }
}