package sejong.foodsns.dto.board.recommend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class BoardRecommendDownDto {

    @Schema(description = "유저 이름")
    private String username;
    @Schema(description = "게시물 id")
    private Long boardId;
}
