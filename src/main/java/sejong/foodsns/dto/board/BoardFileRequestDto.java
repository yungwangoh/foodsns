package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BoardFileRequestDto {
    private Long id;
    @NotBlank
    private String filename;
    private UUID uuid;
    private String filePath;
    private Board board;

    public BoardFile toEntity() {
        return BoardFile.builder()
                .originFileName(filename)
                .uuid(uuid)
                .board(board)
                .build();
    }

    public BoardFileRequestDto(String fileName, UUID uuid,
                               String filePath, Board board) {
        this.filename = fileName;
        this.uuid = uuid;
        this.filePath = filePath;
        this.board = board;
    }
}
