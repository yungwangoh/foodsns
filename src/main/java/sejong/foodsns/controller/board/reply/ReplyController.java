package sejong.foodsns.controller.board.reply;

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
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.reply.ReplyRequestDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.reply.ReplyUpdateDto;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static sejong.foodsns.service.board.crud.message.ReplySuccessOrFailedMessage.REPLY_SUCCESS_DELETE;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReplyController {

    private final ReplyCrudService replyCrudService;

    /**
     * 대댓글 등록
     * @param replyRequestDto 대댯글 등록 dto : content, commentId, user email
     * @return 대댓글 응답 dto
     */
    @Operation(summary = "대댓글 등록")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "대댓글 등록 성공", content = @Content(schema = @Schema(implementation = ReplyResponseDto.class)))
    )
    @PostMapping("/reply")
    ResponseEntity<ReplyResponseDto> replyCreate(@RequestBody @Valid ReplyRequestDto replyRequestDto) {

        ResponseEntity<Optional<ReplyResponseDto>> replyCreate =
                replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        return new ResponseEntity<>(getReplyResponseDto(replyCreate), replyCreate.getStatusCode());
    }

    /**
     * 대댓글 수정
     * @param replyUpdateDto 대댓글 수정 dto : replyId, updateContent
     * @return 대댓글 응답 dto
     */
    @Operation(summary = "대댓글 수정")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "대댓글 수정 성공", content = @Content(schema = @Schema(implementation = ReplyResponseDto.class)))
    )
    @PatchMapping("/reply")
    ResponseEntity<ReplyResponseDto> replyUpdate(@RequestBody @Valid ReplyUpdateDto replyUpdateDto) {

        ResponseEntity<Optional<ReplyResponseDto>> replyUpdate =
                replyCrudService.replyContentUpdateById(replyUpdateDto.getReplyId(), replyUpdateDto.getUpdateContent());

        return new ResponseEntity<>(getReplyResponseDto(replyUpdate), replyUpdate.getStatusCode());
    }

    /**
     * 대댓글 삭제
     * @param replyId 대댓글 id
     * @return NO_CONTENT
     */
    @Operation(summary = "대댓글 삭제")
    @ApiResponses(
            @ApiResponse(responseCode = "204", description = "대댓글 삭제 완료", content = @Content(schema = @Schema(implementation = String.class)))
    )
    @DeleteMapping("/reply/{replyId}")
    ResponseEntity<String> replyDelete(@PathVariable Long replyId) {

        ResponseEntity<Optional<ReplyResponseDto>> replyDelete = replyCrudService.replyDelete(replyId);

        return new ResponseEntity<>(REPLY_SUCCESS_DELETE, replyDelete.getStatusCode());
    }

    /**
     * 대댓글 검색 id를 통하여
     * @param replyId 대댓글 id
     * @return 대댓글 응답 dto
     */
    @Operation(summary = "대댓글 검색 -> (id)")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "대댓글 검색 성공 id로 검색", content = @Content(schema = @Schema(implementation = ReplyResponseDto.class)))
    )
    @GetMapping("/reply/{replyId}")
    ResponseEntity<ReplyResponseDto> replySearch(@PathVariable Long replyId) {

        ResponseEntity<Optional<ReplyResponseDto>> reply = replyCrudService.findReplyById(replyId);

        return new ResponseEntity<>(getReplyResponseDto(reply), reply.getStatusCode());
    }

    /**
     * 대댓글 내용으로 검색하여 리스트 출력 (한글자라도 포함하면..)
     * @param content 대댓글 내용
     * @return 대댓글 응답 리스트
     */
    @Operation(summary = "대댓글 검색", description = "내용으로 검색 (한글자라도 포함하면..)")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "대댓글 검색 성공", content = @Content(schema = @Schema(implementation = ReplyResponseDto.class)))
    )
    @GetMapping("/replies/content")
    ResponseEntity<List<ReplyResponseDto>> replySearchByContent(@RequestParam("content") String content) {

        ResponseEntity<Optional<List<ReplyResponseDto>>> repliesByContent =
                replyCrudService.findRepliesByContent(content);

        return new ResponseEntity<>(repliesByContent.getBody().get(), repliesByContent.getStatusCode());
    }

    /**
     * 대댓글 닉네임으로 검색
     * @param username 닉네임
     * @return 대댓글 리스트
     */
    @Operation(summary = "대댓글 검색", description = "닉네임으로 검색")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "대댓글 검색 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    )
    @GetMapping("/replies/username")
    ResponseEntity<List<ReplyResponseDto>> replySearchByUserName(@RequestParam("username") String username) {

        ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByUsername =
                replyCrudService.replyListByUsername(username);

        return new ResponseEntity<>(replyListByUsername.getBody().get(), replyListByUsername.getStatusCode());
    }

    private static ReplyResponseDto getReplyResponseDto(ResponseEntity<Optional<ReplyResponseDto>> reply) {
        return reply.getBody().get();
    }
}
