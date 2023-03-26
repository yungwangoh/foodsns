package sejong.foodsns.dto.search;

import lombok.*;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class IntegratedSearchResponseDto {

    private List<BoardResponseDto> boardResponseDtos;
    private List<CommentResponseDto> commentResponseDtos;
    private List<ReplyResponseDto> replyResponseDtos;
}
