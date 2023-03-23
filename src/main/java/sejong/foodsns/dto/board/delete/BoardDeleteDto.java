package sejong.foodsns.dto.board.delete;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDeleteDto {

    @Schema(description = "게시물 id")
    private Long boardId;

    @Schema(description = "닉네임", example = "흐미")
    private String username;
}
