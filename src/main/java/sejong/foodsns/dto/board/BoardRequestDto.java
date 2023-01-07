package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardRequestDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private Member member;

    private Long check;

    private int recommCount;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .memberRank(member.getMemberRank())
                .check(0L)
                .recommCount(0)
                .member(member)
                .build();
    }
}
