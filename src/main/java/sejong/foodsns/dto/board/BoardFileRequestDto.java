package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BoardFileRequestDto {
    @NotBlank
    private String originFilename;
    private UUID uuid;
    private String filePath;
    private Board board;

    public BoardFile toEntity() {
        return BoardFile.builder()
                .originFileName(originFilename)
                .uuid(uuid)
                .board(board)
                .build();
    }

}
