package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

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

    private List<BoardFile> boardFiles;

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
