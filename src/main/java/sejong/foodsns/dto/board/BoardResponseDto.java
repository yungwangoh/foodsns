package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.lang.Nullable;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.FoodTag;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;

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

    private Member member;

    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberRank = member.getMemberRank();
        this.check = board.getCheck();
        this.recommCount = board.getRecommCount();
        this.member = board.getMember();
    }
}
