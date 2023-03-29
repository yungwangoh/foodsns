package sejong.foodsns.dto.file;

import lombok.*;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;

import static lombok.AccessLevel.*;

@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class BoardFileResponseDto {

    private Long id;
    private Long boardId;
    private String originalFileName;
    private String storeFileName;
    private String uploadUsername;

    public BoardFileResponseDto(BoardFile boardFile) {
        this.id = boardFile.getId();
        this.boardId = boardFile.getBoard().getId();
        this.originalFileName = boardFile.getOriginFilename();
        this.storeFileName = boardFile.getStoreFileName();
        this.uploadUsername = boardFile.getBoard().getMember().getUsername();
    }
}
