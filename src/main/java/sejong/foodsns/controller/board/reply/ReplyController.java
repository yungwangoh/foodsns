package sejong.foodsns.controller.board.reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.reply.ReplyRequestDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.reply.ReplyUpdateDto;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import javax.validation.Valid;
import java.util.Optional;

import static java.util.Optional.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReplyController {

    private final ReplyCrudService replyCrudService;

    @PostMapping("/reply")
    ResponseEntity<ReplyResponseDto> replyCreate(@RequestBody @Valid ReplyRequestDto replyRequestDto) {

        ResponseEntity<Optional<ReplyResponseDto>> replyCreate =
                replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        return new ResponseEntity<>(getReplyResponseDto(replyCreate), replyCreate.getStatusCode());
    }

    @PatchMapping("/reply")
    ResponseEntity<ReplyResponseDto> replyUpdate(@RequestBody @Valid ReplyUpdateDto replyUpdateDto) {

        ResponseEntity<Optional<ReplyResponseDto>> replyUpdate =
                replyCrudService.replyContentUpdateById(replyUpdateDto.getReplyId(), replyUpdateDto.getUpdateContent());

        return new ResponseEntity<>(getReplyResponseDto(replyUpdate), replyUpdate.getStatusCode());
    }

    @DeleteMapping("/reply/{replyId}")
    ResponseEntity<ReplyResponseDto> replyDelete(@PathVariable Long replyId) {

        ResponseEntity<Optional<ReplyResponseDto>> replyDelete = replyCrudService.replyDelete(replyId);

        return new ResponseEntity<>(getReplyResponseDto(replyDelete), replyDelete.getStatusCode());
    }

    @GetMapping("/reply/{replyId}")
    ResponseEntity<ReplyResponseDto> replySearch(@PathVariable Long replyId) {

        ResponseEntity<Optional<ReplyResponseDto>> reply = replyCrudService.findReplyById(replyId);

        return new ResponseEntity<>(getReplyResponseDto(reply), reply.getStatusCode());
    }

    private static ReplyResponseDto getReplyResponseDto(ResponseEntity<Optional<ReplyResponseDto>> replyCreate) {
        return replyCreate.getBody().get();
    }
}
