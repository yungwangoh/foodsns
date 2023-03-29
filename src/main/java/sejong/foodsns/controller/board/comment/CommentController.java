package sejong.foodsns.controller.board.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.comment.CommentRequestDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.board.update.CommentUpdateContentDto;
import sejong.foodsns.service.board.crud.CommentCrudService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static sejong.foodsns.service.board.crud.message.CommentSuccessOrFailedMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {

    private final CommentCrudService commentCrudService;

    /**
     * 댓글 등록
     * @param commentRequestDto
     * @return 댓글, CREATE
     */
    @Operation(summary = "댓글 등록", description = "댓글을 등록한다.")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "댓글 등록에 성공하였습니다.", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> commentCreate(@RequestBody @Valid CommentRequestDto commentRequestDto) {
        ResponseEntity<Optional<CommentResponseDto>> commentCreate =
                commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

        return new ResponseEntity<>(getComment(commentCreate), commentCreate.getStatusCode());
    }

    /**
     * 전체 댓글 목록 조회
     * @return 댓글 목록, OK
     */
    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회한다.")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> boards() {

        ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.allCommentList();

        return new ResponseEntity<>(getCommentResponseDtos(commentList), commentList.getStatusCode());
    }

    /**
     * 댓글 id로 검색
     * @param commentId 댓글 id
     * @return 댓글, OK
     */
    @Operation(summary = "댓글 id로 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 검색 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> commentSearchById(@PathVariable Long commentId) {

        ResponseEntity<Optional<CommentResponseDto>> comment = commentCrudService.findCommentById(commentId);

        return new ResponseEntity<>(getComment(comment), comment.getStatusCode());
    }

    /**
     * 회원명으로 댓글 목록 검색
     * @param username 닉네임
     * @return 댓글 목록, OK
     */
    @Operation(summary = "회원명으로 댓글 목록 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 검색 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/comment/search/username")
    public ResponseEntity<List<CommentResponseDto>> commentsSearchByUsername(@RequestParam("username") String username) {

        ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.commentListByUsername(username);

        return new ResponseEntity<>(getCommentResponseDtos(commentList), commentList.getStatusCode());
    }

    /**
     * 게시물 제목으로 댓글 목록 검색
     * @param title
     * @return 댓글 목록, OK
     */
    @Operation(summary = "게시물 제목으로 댓글 목록 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 검색 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/comment/search/board-title")
    public ResponseEntity<List<CommentResponseDto>> commentsSearchByTitle(@RequestParam("board-title") String title) {

        ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.commentListByBoardTitle(title);

        return new ResponseEntity<>(getCommentResponseDtos(commentList), commentList.getStatusCode());
    }

    /**
     * 내용으로 댓글 리스트 출력
     * @param content 내용
     * @return 댓글 리스트
     */
    @Operation(summary = "내용으로 댓글 리스트 출력")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 리스트 출력 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/comments/search/content")
    ResponseEntity<List<CommentResponseDto>> commentSearchByContent(@RequestParam("content") String content) {

        ResponseEntity<Optional<List<CommentResponseDto>>> commentsByContent =
                commentCrudService.findCommentsByContent(content);

        return new ResponseEntity<>(getCommentResponseDtos(commentsByContent), commentsByContent.getStatusCode());
    }

    /**
     * 댓글 내용 수정
     * @param commentUpdateContentDto
     * @return 댓글 내용 수정 완료, OK
     */
    @Operation(summary = "댓글 내용 수정")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = String.class)))
    )
    @PatchMapping("/comment")
    public ResponseEntity<String> commentUpdateContent(@RequestBody @Valid CommentUpdateContentDto commentUpdateContentDto) {

        ResponseEntity<Optional<CommentResponseDto>> commentUpdate =
                commentCrudService.commentContentUpdate(commentUpdateContentDto.getTitle(), commentUpdateContentDto.getContent(),
                        commentUpdateContentDto.getUpdateContent());

        return new ResponseEntity<>(COMMENT_CONTENT_UPDATE_SUCCESS, commentUpdate.getStatusCode());
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 id
     * @return 게시물 삭제 완료, OK
     */
    @Operation(summary = "댓글 삭제")
    @ApiResponses(
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = String.class)))
    )
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> commentDelete(@PathVariable Long commentId) {

        ResponseEntity<Optional<CommentResponseDto>> commentDelete = commentCrudService.commentDelete(commentId);

        return new ResponseEntity<>(COMMENT_DELETE_SUCCESS, commentDelete.getStatusCode());
    }

    /**
     * 댓글 목록 Optional Wrapping 해제 후 반환
     * @param commentList
     * @return 댓글 목록
     */
    private List<CommentResponseDto> getCommentResponseDtos(ResponseEntity<Optional<List<CommentResponseDto>>> commentList) {
        return commentList.getBody().get();
    }

    /**
     * 댓글 Dto Optional Wrapping 해제 후 반환
     * @param comment
     * @return 댓글 응답 Dto
     */
    private CommentResponseDto getComment(ResponseEntity<Optional<CommentResponseDto>> comment) {
        return getBody(comment).get();
    }

    /**
     * ResponseEntity Wrapping 해제 후 반환
     * @param commentCreate
     * @return Optionally Wrapped 댓글 응답 Dto
     */
    private Optional<CommentResponseDto> getBody(ResponseEntity<Optional<CommentResponseDto>> commentCreate) {
        return commentCreate.getBody();
    }
}
