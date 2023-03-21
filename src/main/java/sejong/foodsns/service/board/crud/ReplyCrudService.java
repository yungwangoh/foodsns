package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.reply.ReplyResponseDto;

import java.util.List;
import java.util.Optional;

public interface ReplyCrudService {

    ResponseEntity<Optional<ReplyResponseDto>> replyCreate(String content, Long commentId, String email);

    ResponseEntity<Optional<ReplyResponseDto>> replyContentUpdate(String title, String updateContent, String orderContent);

    ResponseEntity<Optional<ReplyResponseDto>> replyContentUpdateById(Long replyId, String updateContent);

    ResponseEntity<Optional<ReplyResponseDto>> replyDelete(Long replyId);

    ResponseEntity<Optional<ReplyResponseDto>> findReply(String title, String content);

    ResponseEntity<Optional<ReplyResponseDto>> findReplyById(Long replyId);

    ResponseEntity<Optional<List<ReplyResponseDto>>> allReplyList();

    ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByUsername(String username);

    ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByBoardTitle(String title);

    Boolean replyContentExistValidation(ReplyResponseDto replyResponseDto);

}
