package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.board.CommentRequestDto;
import sejong.foodsns.dto.board.CommentResponseDto;

import java.util.List;
import java.util.Optional;

public interface CommentCrudService {

    ResponseEntity<Optional<CommentResponseDto>> commentCreate(CommentRequestDto commentRequestDto);

    ResponseEntity<Optional<CommentResponseDto>> commentContentUpdate(String title, String updateContent, String orderContent);

    ResponseEntity<Optional<CommentResponseDto>> contentDelete(CommentRequestDto commentRequestDto);

    ResponseEntity<Optional<CommentResponseDto>> findComment(String title, String content);

    ResponseEntity<Optional<List<CommentResponseDto>>> commentList();

    Boolean commentContentExistValidation(CommentRequestDto commentRequestDto);

}
