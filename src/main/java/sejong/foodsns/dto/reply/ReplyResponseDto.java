package sejong.foodsns.dto.reply;

import lombok.*;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.member.MemberResponseDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReplyResponseDto {

    private Long id;
    private String content;
    private int recommCount;
    private int reportCount;
    private MemberResponseDto memberResponseDto;

    @Builder
    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.recommCount = reply.getRecommCount();
        this.reportCount = reply.getReportCount();
        this.memberResponseDto = new MemberResponseDto(reply.getMember());
    }
}
