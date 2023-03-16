package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.CommentRequestDto;
import sejong.foodsns.dto.board.CommentResponseDto;
import sejong.foodsns.dto.board.ReplyRequestDto;
import sejong.foodsns.dto.board.ReplyResponseDto;

import java.util.List;
import java.util.Optional;

public interface ReplyCrudService {

    ResponseEntity<Optional<ReplyResponseDto>> replyCreate(ReplyRequestDto replyRequestDto);

    ResponseEntity<Optional<ReplyResponseDto>> replyContentUpdate(String title, String updateContent, String orderContent);

    ResponseEntity<Optional<ReplyResponseDto>> replyDelete(Long id);

    ResponseEntity<Optional<ReplyResponseDto>> findReply(String title, String content);

    ResponseEntity<Optional<ReplyResponseDto>> findReplyById(Long id);

    ResponseEntity<Optional<List<ReplyResponseDto>>> allReplyList();

    ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByUsername(String username);

    ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByBoardTitle(String title);

    Boolean replyContentExistValidation(ReplyResponseDto replyResponseDto);

}
