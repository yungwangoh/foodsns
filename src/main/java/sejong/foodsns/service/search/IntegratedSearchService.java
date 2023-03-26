package sejong.foodsns.service.search;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.search.IntegratedSearchResponseDto;

import java.util.List;

public interface IntegratedSearchService {

    ResponseEntity<IntegratedSearchResponseDto> integratedSearch(String content);
    ResponseEntity<List<BoardResponseDto>> boardIntegratedSearch(String content);
    ResponseEntity<List<CommentResponseDto>> commentIntegratedSearch(String content);
    ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearch(String content);
}
