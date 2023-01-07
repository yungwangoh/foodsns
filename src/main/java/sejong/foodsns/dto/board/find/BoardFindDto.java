package sejong.foodsns.dto.board.find;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sejong.foodsns.domain.member.Member;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class BoardFindDto {

    @NotBlank
    private String title;

    @NotBlank
    private Member member;
}
