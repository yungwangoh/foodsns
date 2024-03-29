package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.comment.CommentRequestDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.exception.http.board.NoSearchCommentException;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.querydsl.comment.CommentQueryRepository;
import sejong.foodsns.service.board.crud.CommentCrudService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentCrudServiceImpl implements CommentCrudService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentQueryRepository commentQueryDslRepository;

    /**
     * 댓글 생성 -> 성공 ?, 실패 ?
     * @param content 댓글 내용
     * @param boardId 게시물 아이디
     * @param email 유저 이메일
     * @return 댓글 DTO
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<CommentResponseDto>> commentCreate(String content, Long boardId, String email) {

        Comment comment = commentClassCreated(content, boardId, email);

        Comment saveComment = commentRepository.save(comment);

        return new ResponseEntity<>(of(new CommentResponseDto(saveComment)), CREATED);
    }

    /**
     * 댓글 찾기 -> 성공 ?, 실패 ?
     * @param title
     * @return 댓글, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<CommentResponseDto>> findComment(String boardTitle, String content) {
        Optional<Comment> comment = commentRepository.findByBoardTitleAndContainingContent(boardTitle, content);

        return new ResponseEntity<>(of(new CommentResponseDto(getComment(comment))), OK);
    }

    /**
     * 모든 댓글 목록 -> 성공 ?
     * @return 댓글 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<CommentResponseDto>>> allCommentList() {
        List<Comment> comments = commentRepository.findAll();

        Optional<List<CommentResponseDto>> collect = of(comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 댓글 찾기
     * @param commentId 댓글 id
     * @return 댓글, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<CommentResponseDto>> findCommentById(Long commentId) {
        Optional<Comment> comment = getCommentReturnByCommentId(commentId);

        return new ResponseEntity<>(of(new CommentResponseDto(comment.get())), OK);
    }

    /**
     * 회원이 작성한 댓글 목록 -> 성공 ?
     * @return 회원이 작성한 댓글 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<CommentResponseDto>>> commentListByUsername(String username) {
        List<Comment> comments = commentRepository.findCommentsByUsername(username);

        Optional<List<CommentResponseDto>> collect = of(comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(collect, OK);
    }

    @Override
    public ResponseEntity<Optional<List<CommentResponseDto>>> findCommentsByContent(String content) {

        List<Comment> comments = commentQueryDslRepository.searchComments(content);

        Optional<List<CommentResponseDto>> commentResponseDtos = of(comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(commentResponseDtos, OK);
    }

    /**
     * 게시물에 작성된 댓글 목록 -> 성공 ?
     * @return 게시물에 작성된 댓글 리스트, HTTP OK
     */

    @Override
    public ResponseEntity<Optional<List<CommentResponseDto>>> commentListByBoardTitle(String title) {
        List<Comment> comments = commentRepository.findCommentsByBoardTitle(title);

        Optional<List<CommentResponseDto>> commentList = of(comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(commentList, OK);
    }

    /**
     * 댓글 내용 수정 -> 성공 ?, 실패 ?
     * @param title
     * @param updateContent
     * @param orderContent
     * @return 댓글 DTO, HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<CommentResponseDto>> commentContentUpdate(String title, String updateContent, String orderContent) {
        Optional<Comment> findComment = getCommentReturnByOptionalComment(title, orderContent);

        Comment updateComment = getComment(findComment).commentContentUpdate(updateContent);

        Comment saveComment = commentRepository.save(updateComment);

        return new ResponseEntity<>(of(new CommentResponseDto(saveComment)), OK);

    }

    /**
     * 댓글 삭제 -> 성공 ?, 실패 ?
     * @param commentRequestDto
     * @return HTTP OK
     */

    @Override
    @Transactional
    public ResponseEntity<Optional<CommentResponseDto>> commentDelete(Long commentId) {
        Optional<Comment> comment = getCommentReturnByCommentId(commentId);

        commentRepository.delete(comment.get());

        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * Optional Comment -> return comment
     * @param comment
     * @return 댓글
     */
    private Comment getComment(Optional<Comment> comment) {
        return comment.get();
    }

    /**
     * 댓글 객체 생성
     * @param commentRequestDto
     * @return 댓글
     */
    private Comment commentClassCreated(String content, Long id, String email) {

        Optional<Board> board = boardRepository.findById(id);
        Optional<Member> member = memberRepository.findMemberByEmail(email);

        return Comment.builder()
                .content(content)
                .reportCount(0)
                .recommCount(0)
                .board(board.get())
                .member(member.get())
                .build();
    }

    /**
     * 댓글 반환하는 로직 List
     * @param commentId
     * @return 댓글 존재 X -> Exception
     */
    private Optional<Comment> getCommentReturnByCommentId(Long commentId) {
        return of(commentRepository.findById(commentId))
                .orElseThrow(() -> new NoSearchCommentException("댓글이 존재하지 않습니다."));
    }

    private Optional<Comment> getCommentReturnByOptionalComment(String title, String content) {
        return of(commentRepository.findByBoardTitleAndContainingContent(title, content))
                .orElseThrow(() -> new NoSearchCommentException("댓글이 존재하지 않습니다."));
    }

    @Override
    public Boolean commentContentExistValidation(CommentRequestDto commentRequestDto) {
        return null;
    }
}
