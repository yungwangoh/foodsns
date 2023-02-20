package sejong.foodsns.dto.board;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;

import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardFileResponseDto {

    private Long id;
    private UUID uuid;
    private String fileName;
    private BoardResponseDto boardResponseDto;

    @Builder
    public BoardFileResponseDto(BoardFile boardFile) {
        this.id = boardFile.getId();
        this.uuid = boardFile.getUuid();
        this.fileName = boardFile.getOriginFilename();
        this.boardResponseDto = new BoardResponseDto(boardFile.getBoard());
    }
}
