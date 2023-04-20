package sejong.foodsns.dto.board.recommend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardRecommendUpDto {

    @Schema(description = "유저 이름")
    private String username;
    @Schema(description = "게시물 id")
    private Long boardId;
}
