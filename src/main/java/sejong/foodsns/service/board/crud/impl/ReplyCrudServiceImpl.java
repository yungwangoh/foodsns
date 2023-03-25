package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.exception.http.board.NoSearchReplyException;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.querydsl.reply.ReplyQueryRepository;
import sejong.foodsns.service.board.crud.ReplyCrudService;

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
    private final MemberRepository memberRepository;
    private final ReplyQueryRepository replyQueryDslRepository;

    /**
     * 대댓글 생성 -> 성공 401, 실패 404
     * @param content 내용
     * @param commentId 댓글 id
     * @param email 유저 이메일
     * @return 대댓글 DTO
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyCreate(String content, Long commentId, String email) {

        Reply reply = replyClassCreated(content, commentId, email);
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
        Optional<Reply> findReply = of(getReplyReturnByOptionalReply(title, orderContent)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다.")));

        Reply updateReply = getReply(findReply).replyContentUpdate(updateContent);

        return new ResponseEntity<>(of(new ReplyResponseDto(updateReply)), OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyContentUpdateById(Long replyId, String updateContent) {
        Optional<Reply> reply = getReplyReturnByReplyId(replyId);

        Reply updateReply = getReply(reply).replyContentUpdate(updateContent);

        return new ResponseEntity<>(of(new ReplyResponseDto(updateReply)), OK);
    }

    /**
     * 대댓글 삭제 -> 성공 200, 실패 404
     * @param replyId 대댓글 id
     * @return HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<ReplyResponseDto>> replyDelete(Long replyId) {
        Optional<Reply> findReply = getReplyReturnByReplyId(replyId);

        replyRepository.delete(getReply(findReply));
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * 대댓글 찾기 -> 성공 200, 실패 404
     * @param title 제목
     * @param content 내용
     * @return 게시물, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<ReplyResponseDto>> findReply(String title, String content) {

        Optional<Reply> reply = of(replyRepository.findByBoardTitleAndContainingContent(title, content)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다.")));

        return new ResponseEntity<>(of(new ReplyResponseDto(getReply(reply))), OK);

    }

    /**
     * id를 통해서 대댓글 찾기
     * @param replyId 대댓글 id
     * @return 대댓글, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<ReplyResponseDto>> findReplyById(Long replyId) {
        Optional<Reply> reply = getReplyReturnByReplyId(replyId);

        return ResponseEntity.status(OK).body(of(new ReplyResponseDto(getReply(reply))));
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

    @Override
    public ResponseEntity<Optional<List<ReplyResponseDto>>> findRepliesByContent(String content) {
        List<Reply> replies = replyQueryDslRepository.searchReplies(content);

        Optional<List<ReplyResponseDto>> replyResponseDtos = of(replies.stream()
                .map(ReplyResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(replyResponseDtos, OK);
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
    private static Reply getReply(Optional<Reply> reply) {
        return reply.get();
    }

    /**
     * 대댓글 객체 생성
     * @param content 내용
     * @param commentId 댓글 id
     * @param email 유저 이메일
     * @return 대댓글
     */
    private Reply replyClassCreated(String content, Long commentId, String email) {

        Optional<Comment> comment = of(commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")));

        Optional<Member> member = of(memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")));

        return Reply.builder()
                .content(content)
                .recommCount(0)
                .reportCount(0)
                .comment(comment.get())
                .member(member.get())
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
