package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentRequestDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.CommentCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
public class CommentCrudServiceImplTest {

    @Autowired private CommentCrudService commentCrudService;
    @Autowired private BoardCrudService boardCrudService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private MemberRepository memberRepository;

    private BoardResponseDto boardResponseDto;

    @BeforeEach
    void initMemberAndBoard() {
        Member member = new Member("하윤", "gkdbssla97@naver.com", "4321", NORMAL);
        memberRepository.save(member);
        Board board = new Board("레시피1", "콩나물무침", 13L, 13, null,
                member);
        boardRepository.save(board);
    }

    @Nested
    @DisplayName("서비스 성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ServiceSuccess {
        @Test
        @DisplayName("댓글 등록")
        void commentCreate() {

            //given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();
            CommentRequestDto commentRequestDto = getCommentRequestDto(1, member, board);

            //when
            ResponseEntity<Optional<CommentResponseDto>> commentCreate = commentCrudService
                    .commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

            //then
            assertThat(commentCreate.getStatusCode()).isEqualTo(CREATED);
            assertThat(getBody(commentCreate).getContent()).isEqualTo(commentRequestDto.getContent());
        }

        @Test
        @DisplayName("댓글 찾기")
        void findComment() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto = getCommentRequestDto(1, member, board);
            commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

            // when
            ResponseEntity<Optional<CommentResponseDto>> findComment = commentCrudService.findComment(board.getTitle(), commentRequestDto.getContent());

            // then
            assertThat(findComment.getStatusCode()).isEqualTo(OK);
            assertTrue(getFindCommentBody(findComment).isPresent());
        }

        @Test
        @DisplayName("댓글 목록")
        void commentList() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto1 = getCommentRequestDto(1, member, board);
            CommentRequestDto commentRequestDto2 = getCommentRequestDto(2, member, board);

            List<ResponseEntity<Optional<CommentResponseDto>>> list = new ArrayList<>();
            list.add(commentCrudService.commentCreate(commentRequestDto1.getContent(), commentRequestDto1.getBoardId(), commentRequestDto1.getEmail()));
            list.add(commentCrudService.commentCreate(commentRequestDto2.getContent(), commentRequestDto2.getBoardId(), commentRequestDto2.getEmail()));

            // when
            ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.allCommentList();

            // then
            assertThat(commentList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getCommentResponseDtos(commentList).size());
            assertThat(getCommentResponseDtos(commentList).size()).isEqualTo(2);
        }

        @Test
        @DisplayName("회원 이름으로 검색한 댓글 목록")
        void commentListByUsername() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto1 = getCommentRequestDto(1, member, board);
            CommentRequestDto commentRequestDto2 = getCommentRequestDto(2, member, board);

            List<ResponseEntity<Optional<CommentResponseDto>>> list = new ArrayList<>();
            list.add(commentCrudService.commentCreate(commentRequestDto1.getContent(), commentRequestDto1.getBoardId(), commentRequestDto1.getEmail()));
            list.add(commentCrudService.commentCreate(commentRequestDto2.getContent(), commentRequestDto2.getBoardId(), commentRequestDto2.getEmail()));

            // when
            ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.commentListByUsername(member.getUsername());

            // then
            assertThat(commentList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getCommentResponseDtos(commentList).size());
            assertThat(getCommentResponseDtos(commentList).size()).isEqualTo(2);
        }

        @Test
        @DisplayName("게시물 제목으로 검색한 댓글 목록")
        void commentListByBoardTitle() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto1 = getCommentRequestDto(1, member, board);
            CommentRequestDto commentRequestDto2 = getCommentRequestDto(2, member, board);

            List<ResponseEntity<Optional<CommentResponseDto>>> list = new ArrayList<>();
            list.add(commentCrudService.commentCreate(commentRequestDto1.getContent(), commentRequestDto1.getBoardId(), commentRequestDto1.getEmail()));
            list.add(commentCrudService.commentCreate(commentRequestDto2.getContent(), commentRequestDto2.getBoardId(), commentRequestDto2.getEmail()));

            // when
            ResponseEntity<Optional<List<CommentResponseDto>>> commentList = commentCrudService.commentListByBoardTitle(board.getTitle());

            // then
            assertThat(commentList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getCommentResponseDtos(commentList).size());
        }

        private List<CommentResponseDto> getCommentResponseDtos(ResponseEntity<Optional<List<CommentResponseDto>>> commentList) {
            return commentList.getBody().get();
        }

        @Test
        @DisplayName("댓글 내용 수정")
        void commentContentUpdate() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            String updateContent = "레시피가 업데이트 됐네요?";
            CommentRequestDto commentRequestDto = getCommentRequestDto(1, member, board);
            commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

            ResponseEntity<Optional<CommentResponseDto>> commentContentUpdate =
                    commentCrudService.commentContentUpdate(board.getTitle(), updateContent, commentRequestDto.getContent());

            Comment comment = commentRepository.findByBoardTitleAndContainingContent(board.getTitle(), "레시피").get();
            // then
            assertThat(commentContentUpdate.getStatusCode()).isEqualTo(OK);
            assertThat(comment.getContent()).isEqualTo(updateContent);
        }

        @Test
        @DisplayName("댓글 삭제")
        void commentDelete() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto = getCommentRequestDto(1, member, board);
            ResponseEntity<Optional<CommentResponseDto>> commentCreate =
                    commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

            // when
            commentCrudService.commentDelete(getBody(commentCreate).getId());

            // then
            // 찾으려는 댓글이 없어야한다.
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<CommentResponseDto>> comment =
                        commentCrudService.findComment("레시피1", commentRequestDto.getContent());

                assertThat(comment.getStatusCode()).isEqualTo(NO_CONTENT);
            }).isInstanceOf(NoSuchElementException.class);
        }

        @AfterEach
        void deleteInit() {
            memberRepository.deleteAll();
            boardRepository.deleteAll();
            commentRepository.deleteAll();
        }
    }

    @Nested
    @DisplayName("서비스 실패")
    class serviceFail {

        @Test
        @DisplayName("찾으려는 댓글이 존재하지 않을때 예외")
        void commentFindException() {
            // given
            Member member = memberRepository.findMemberByUsername("하윤").get();
            Board board = boardRepository.findBoardByTitle("레시피1").get();

            CommentRequestDto commentRequestDto = getCommentRequestDto(1, member, board);

            // when
            commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());

            // then
            assertThatThrownBy(() -> commentCrudService.findComment(board.getTitle(), getCommentRequestDto(2, member, board).getContent()))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @AfterEach
        void deleteInit() {
            memberRepository.deleteAll();
            boardRepository.deleteAll();
            commentRepository.deleteAll();
        }
    }

    private CommentRequestDto getCommentRequestDto(int idx, Member member, Board board) {

        if (idx == 1) {
            return CommentRequestDto.builder()
                    .content("맛있네요")
                    .boardId(board.getId())
                    .email(member.getEmail())
                    .build();
        }
        return CommentRequestDto.builder()
                .content("맛없네요")
                .boardId(board.getId())
                .email(member.getEmail())
                .build();
    }

    private CommentResponseDto getBody(ResponseEntity<Optional<CommentResponseDto>> commentCreate) {
        return getFindCommentBody(commentCreate).get();
    }

    private Optional<CommentResponseDto> getFindCommentBody(ResponseEntity<Optional<CommentResponseDto>> findComment) {
        return findComment.getBody();
    }
}
