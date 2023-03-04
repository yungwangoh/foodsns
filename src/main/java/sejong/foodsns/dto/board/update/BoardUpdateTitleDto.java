package sejong.foodsns.dto.board.update;

import lombok.*;
import lombok.extern.java.Log;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardUpdateTitleDto {

    private Long id;
    private MemberRequestDto memberRequestDto;
    private String orderTitle;
    private String updateTitle;
}
