package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.lang.Nullable;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.FoodTag;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.MemberResponseDto;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;

    private String title;

    private String content;

    private MemberRank memberRank;

    private Long check;

    private int recommCount;

    private MemberResponseDto memberResponseDto;

    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberResponseDto = new MemberResponseDto(board.getMember());
        this.memberRank = board.getMemberRank();
        this.check = board.getCheck();
        this.recommCount = board.getRecommCount();
    }
}
