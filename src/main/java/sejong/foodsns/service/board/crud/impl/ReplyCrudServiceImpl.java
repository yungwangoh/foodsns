package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.CommentRequestDto;
import sejong.foodsns.dto.board.CommentResponseDto;
import sejong.foodsns.dto.board.ReplyRequestDto;
import sejong.foodsns.dto.board.ReplyResponseDto;
import sejong.foodsns.exception.http.board.NoSearchCommentException;
import sejong.foodsns.exception.http.board.NoSearchReplyException;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.CommentCrudService;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyCrudServiceImpl implements ReplyCrudService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 대댓글 생성 -> 성공 401, 실패 404
     * @param replyRequestDto
     * @return 대댓글 DTO
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyCreate(ReplyRequestDto replyRequestDto) {
        Reply reply = replyClassCreated(replyRequestDto, replyRequestDto.getCommentRequestDto());
        Reply saveReply = replyRepository.save(reply);

        return new ResponseEntity<>(of(new ReplyResponseDto(saveReply)), CREATED);
    }

    /**
     * 대댓글 내용 수정 -> 성공 200, 실패 404
     * @param title
     * @param updateContent
     * @param orderContent
     * @return 대댓글 DTO, HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyContentUpdate(String title, String updateContent, String orderContent) {
        Optional<Reply> findReply = getReplyReturnByOptionalReply(title, orderContent);

        Reply updateReply = getReply(findReply).replyContentUpdate(updateContent);

        Reply saveReply = replyRepository.save(updateReply);

        return new ResponseEntity<>(of(new ReplyResponseDto(saveReply)), OK);
    }

    /**
     * 대댓글 삭제 -> 성공 200, 실패 404
     * @param replyRequestDto
     * @return HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyDelete(ReplyRequestDto replyRequestDto) {
        Optional<ReplyResponseDto> reply = findReply(replyRequestDto.getCommentRequestDto().getBoardRequestDto().getTitle(), replyRequestDto.getContent()).getBody();
        Optional<Reply> findReply = getReplyReturnByReplyId(reply.get().getId());

        replyRepository.delete(getReply(findReply));
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * 대댓글 찾기 -> 성공 200, 실패 404
     * @param title,content
     * @return 게시물, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<ReplyResponseDto>> findReply(String title, String content) {
        Optional<Reply> reply = replyRepository.findByBoardTitleAndContainingContent(title, content);
        return new ResponseEntity<>(of(new ReplyResponseDto(getReply(reply))), OK);

    }

    /**
     * 모든 대댓글 목록 -> 성공 200
     * @return 대댓 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<ReplyResponseDto>>> allReplyList() {
        List<Reply> replies = replyRepository.findAll();
        Optional<List<ReplyResponseDto>> replyList = of(replies.stream()
                .map(ReplyResponseDto::new)
                .collect(toList()));
        return new ResponseEntity<>(replyList, OK);
    }

    /**
     * 회원이 작성한 대댓글 목록 -> 성공 200
     * @return 회원이 작성한 대댓글 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByUsername(String username) {
        List<Reply> replies = replyRepository.findRepliesByUsername(username);

        Optional<List<ReplyResponseDto>> replyList = of(replies.stream()
                .map(ReplyResponseDto::new)
                .collect(toList()));
        return new ResponseEntity<>(replyList, OK);
    }

    /**
     * 게시물에 작성된 댓글 목록 -> 성공 ?
     * @return 게시물 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<ReplyResponseDto>>> replyListByBoardTitle(String title) {
        List<Reply> replies = replyRepository.findRepliesByBoardTitle(title);

        Optional<List<ReplyResponseDto>> replyList = of(replies.stream()
                .map(ReplyResponseDto::new)
                .collect(toList()));
        return new ResponseEntity<>(replyList, OK);
    }

    /**
     * Optional Reply -> return reply
     * @param reply
     * @return 대댓글
     */
    private Reply getReply(Optional<Reply> reply) {
        return reply.get();
    }

    /**
     * 대댓글 객체 생성
     * @param replyRequestDto
     * @return 대댓글
     */
    private Reply replyClassCreated(ReplyRequestDto replyRequestDto, CommentRequestDto commentRequestDto) {

        return Reply.builder()
                .content(replyRequestDto.getContent())
                .recommCount(0)
                .reportCount(0)
                .comment(commentRequestDto.toEntity())
                .build();
    }

    /**
     * 대댓글 반환하는 로직 List
     * @param replyId
     * @return 대댓글 존재 X -> Exception
     */
    private Optional<Reply> getReplyReturnByReplyId(Long replyId) {
        return of(replyRepository.findById(replyId))
                .orElseThrow(() -> new NoSearchReplyException("대댓글이 존재하지 않습니다."));
    }

    private Optional<Reply> getReplyReturnByOptionalReply(String title, String content) {
        return of(replyRepository.findByBoardTitleAndContainingContent(title, content))
                .orElseThrow(() -> new NoSearchReplyException("대댓글이 존재하지 않습니다."));
    }

    @Override
    public Boolean replyContentExistValidation(ReplyResponseDto replyResponseDto) {
        return null;
    }
}
