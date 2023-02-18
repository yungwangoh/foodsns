package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.BoardFileRequestDto;
import sejong.foodsns.dto.board.BoardFileResponseDto;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;

import java.util.Optional;

public interface BoardFileCrudService {

    ResponseEntity<Optional<BoardFileResponseDto>> boardFileSave(BoardFileRequestDto boardFileRequestDto);

    ResponseEntity<Optional<BoardFileResponseDto>> boardFileGet(BoardFileRequestDto boardFileRequestDto);
}
