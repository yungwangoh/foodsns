package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;
import java.util.Optional;

public interface BoardCrudService {

    ResponseEntity<Optional<BoardResponseDto>> boardCreate(BoardRequestDto boardRequestDto);

    ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate(String email, String username);

    ResponseEntity<Optional<BoardResponseDto>> boardDelete(BoardRequestDto boardRequestDto);

    ResponseEntity<Optional<BoardResponseDto>> findBoard(String title);

    ResponseEntity<Optional<List<BoardResponseDto>>> boardList();

    Boolean boardTitleExistValidation(BoardRequestDto boardRequestDto);

}
