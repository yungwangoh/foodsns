package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.dto.board.BoardFileRequestDto;
import sejong.foodsns.dto.board.BoardFileResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardFileCrudService {

    public List<BoardFile> saveBoardFiles(Map<BoardFileType, List<MultipartFile>> multipartFileListMap) throws IOException;

    public Map<BoardFileType, List<BoardFile>> findBoardFiles();

    ResponseEntity<Optional<BoardFileResponseDto>> boardFileGet(BoardFileRequestDto boardFileRequestDto);
}
