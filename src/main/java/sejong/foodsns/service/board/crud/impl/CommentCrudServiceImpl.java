성package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.board.CommentRequestDto;
import sejong.foodsns.dto.board.CommentResponseDto;
import sejong.foodsns.exception.http.board.NoSearchBoardException;
import sejong.foodsns.exception.http.board.NoSearchCommentException;
import sejong.foodsns.repository.board.CommentRepository;
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

    /**
     * 댓글 생성 -> 성공 ?, 실패 ?
     * @param commentRequestDto
     * @return 댓글 DTO
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<CommentResponseDto>> commentCreate(CommentRequestDto commentRequestDto) {
        Comment comment = commentClassCreated(commentRequestDto);

        Comment saveComment = commentRepository.save(comment);
        return new ResponseEntity<>(of(new CommentResponseDto(saveComment)), CREATED);
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
    public ResponseEntity<Optional<CommentResponseDto>> contentDelete(CommentRequestDto commentRequestDto) {
        Optional<Comment> findComment = getCommentReturnByCommentId(commentRequestDto.getId());

        //Token으로 할 것이므로 Jpa delete 작동하는지만 임시 확인.
        commentRepository.delete(getComment(findComment));

        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * 댓글 찾기 -> 성공 ?, 실패 ?
     * @param title
     * @return 게시물, HTTP OK
     */

    @Override
    public ResponseEntity<Optional<CommentResponseDto>> findComment(String title, String content) {
        Optional<Comment> comment = commentRepository.findByBoardTitleAndContainingContent(title, content);

        return new ResponseEntity<>(of(new CommentResponseDto(getComment(comment))), OK);
    }

    /**
     * 댓글 목록 -> 성공 ?
     * @return 게시물 리스트, HTTP OK
     */

    @Override
    public ResponseEntity<Optional<List<CommentResponseDto>>> commentList() {
        List<Comment> comments = commentRepository.findAll();

        Optional<List<CommentResponseDto>> collect = of(comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(collect, OK);
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
     * @return 게시물
     */
    private Comment commentClassCreated(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .recommCount(commentRequestDto.getRecommCount())
                .reportCount(commentRequestDto.getReportCount())
                .reply(commentRequestDto.getReply())
                .board(commentRequestDto.getBoard())
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
