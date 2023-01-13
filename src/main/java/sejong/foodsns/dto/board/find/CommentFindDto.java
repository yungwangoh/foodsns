package sejong.foodsns.dto.board.find;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class CommentFindDto {

    @NotBlank
    private String content;

    private Board board;

    private Member member;
}
