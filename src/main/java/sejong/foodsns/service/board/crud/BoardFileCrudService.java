package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.dto.board.BoardFileRequestDto;
import sejong.foodsns.dto.board.BoardFileResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardFileCrudService {

    List<BoardFile> saveBoardFiles(List<MultipartFile> multipartFiles) throws IOException;
}
