package sejong.foodsns.controller.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * 통합 검색
     * @param content 검색 내용
     * @return 게시물, 댓글, 회원, 대댓글 리스트
     */
    @Operation(summary = "통합 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "통합 검색 성공", content = @Content(schema = @Schema(implementation = IntegratedSearchResponseDto.class)))
    )
    @GetMapping("/search")
    ResponseEntity<IntegratedSearchResponseDto> integratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<IntegratedSearchResponseDto> integratedSearch = integratedSearchService.integratedSearch(content);

        return new ResponseEntity<>(integratedSearch.getBody(), integratedSearch.getStatusCode());
    }

    /**
     * 게시물 통합 검색
     * @param content 검색 내용
     * @return 게시물 리스트
     */
    @Operation(summary = "게시물 통합 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "게시물 통합 검색 성공", content = @Content(schema = @Schema(implementation = IntegratedSearchResponseDto.class)))
    )
    @GetMapping("/search/board")
    ResponseEntity<List<BoardResponseDto>> boardIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<BoardResponseDto>> boardIntegratedSearch = integratedSearchService.boardIntegratedSearch(content);

        return new ResponseEntity<>(boardIntegratedSearch.getBody(), boardIntegratedSearch.getStatusCode());
    }

    /**
     * 댓글 통합 검색
     * @param content 검색 내용
     * @return 댓글 리스트
     */
    @Operation(summary = "댓글 통합 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 통합 검색 성공", content = @Content(schema = @Schema(implementation = IntegratedSearchResponseDto.class)))
    )
    @GetMapping("/search/comment")
    ResponseEntity<List<CommentResponseDto>> commentIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<CommentResponseDto>> commentIntegratedSearch = integratedSearchService.commentIntegratedSearch(content);

        return new ResponseEntity<>(commentIntegratedSearch.getBody(), commentIntegratedSearch.getStatusCode());
    }

    /**
     * 대댓글 통합 검색
     * @param content 검색 내용
     * @return 대댓글 리스트
     */
    @Operation(summary = "대댓글 통합 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "대댓글 통합 검색 성공", content = @Content(schema = @Schema(implementation = IntegratedSearchResponseDto.class)))
    )
    @GetMapping("/search/reply")
    ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearchController(@RequestParam("content") String content) {

        ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearch = integratedSearchService.replyIntegratedSearch(content);

        return new ResponseEntity<>(replyIntegratedSearch.getBody(), replyIntegratedSearch.getStatusCode());
    }
}
