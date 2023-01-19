package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardRequestDto {

    private Long id;

    private String title;

    private String content;

    private MemberRequestDto memberRequestDto;

    private Long check;

    private int recommCount;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .memberRank(memberRequestDto.toEntity().getMemberRank())
                .check(0L)
                .recommCount(0)
                .member(memberRequestDto.toEntity())
                .build();
    }
}
