package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BoardCrudService {

    ResponseEntity<Optional<BoardResponseDto>> boardCreate(BoardRequestDto boardRequestDto, List<MultipartFile> multipartFiles) throws IOException;

    ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate(Long id, String username, String updateTitle);

    ResponseEntity<Optional<BoardResponseDto>> boardDelete(Long id, String username);

    ResponseEntity<Optional<BoardResponseDto>> findBoardById(Long id);

    ResponseEntity<List<BoardResponseDto>> search(SearchOption searchOption, String content);

    ResponseEntity<Optional<List<BoardResponseDto>>> boardList();

    Boolean boardTitleExistValidation(BoardRequestDto boardRequestDto);

}
