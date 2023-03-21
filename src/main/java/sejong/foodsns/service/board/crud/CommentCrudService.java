package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.comment.CommentRequestDto;
import sejong.foodsns.dto.comment.CommentResponseDto;

import java.util.List;
import java.util.Optional;

public interface CommentCrudService {

    ResponseEntity<Optional<CommentResponseDto>> commentCreate(String content, Long boardId, String email);

    ResponseEntity<Optional<CommentResponseDto>> commentContentUpdate(String title, String updateContent, String orderContent);

    ResponseEntity<Optional<CommentResponseDto>> commentDelete(Long commentId);

    ResponseEntity<Optional<CommentResponseDto>> findComment(String title, String content);

    ResponseEntity<Optional<List<CommentResponseDto>>> allCommentList();

    ResponseEntity<Optional<List<CommentResponseDto>>> findCommentsByContent(String content);

    ResponseEntity<Optional<List<CommentResponseDto>>> commentListByUsername(String username);

    ResponseEntity<Optional<List<CommentResponseDto>>> commentListByBoardTitle(String title);

    Boolean commentContentExistValidation(CommentRequestDto commentRequestDto);

}
