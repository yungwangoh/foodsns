package sejong.foodsns.controller.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.search.IntegratedSearchResponseDto;
import sejong.foodsns.service.search.IntegratedSearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/integration")
public class IntegratedSearchController {

    private final IntegratedSearchService integratedSearchService;

    @GetMapping("/search")
    ResponseEntity<IntegratedSearchResponseDto> integratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<IntegratedSearchResponseDto> integratedSearch = integratedSearchService.integratedSearch(content);

        return new ResponseEntity<>(integratedSearch.getBody(), integratedSearch.getStatusCode());
    }

    @GetMapping("/search/board")
    ResponseEntity<List<BoardResponseDto>> boardIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<BoardResponseDto>> boardIntegratedSearch = integratedSearchService.boardIntegratedSearch(content);

        return new ResponseEntity<>(boardIntegratedSearch.getBody(), boardIntegratedSearch.getStatusCode());
    }

    @GetMapping("/search/comment")
    ResponseEntity<List<CommentResponseDto>> commentIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<CommentResponseDto>> commentIntegratedSearch = integratedSearchService.commentIntegratedSearch(content);

        return new ResponseEntity<>(commentIntegratedSearch.getBody(), commentIntegratedSearch.getStatusCode());
    }

    @GetMapping("/search/reply")
    ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearch = integratedSearchService.replyIntegratedSearch(content);

        return new ResponseEntity<>(replyIntegratedSearch.getBody(), replyIntegratedSearch.getStatusCode());
    }
}
