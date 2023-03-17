package sejong.foodsns.service.board.crud;

import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;

import java.io.IOException;
import java.util.List;

public interface BoardFileCrudService {

    List<BoardFile> saveBoardFiles(List<MultipartFile> multipartFiles, Board board) throws IOException;
}
