package sejong.foodsns.repository.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.FoodTag;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class CommentRepositoryTest {

    @Autowired CommentRepository commentRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;

    List<String> title;
    List<String> content;
    List<String> userName;
    List<String> email;
    List<String> password;
    MemberType memberType;
    MemberRank memberRank;

    @BeforeEach
    void initCommentSetting() {
        this.title = List.of("test1", "test2", "test3");
        this.content = List.of("된장찌개", "김치찌개", "오이초무침");
        this.userName = List.of("윤광오", "하윤", "Naver");
        this.email = List.of("swager253@naver.com", "gkdbssla97@naver.com", "naver@naver.com");
        this.password = List.of("1234", "4321", "1111");
        this.memberType = MemberType.NORMAL;
        this.memberRank = MemberRank.NORMAL;

        Member member = new Member(userName.get(0), email.get(0), password.get(0), memberType);
        memberRepository.save(member);

        Board board = new Board(title.get(0), content.get(0), 13L, 13, null, member);
        boardRepository.save(board);
    }

    @Nested
    @DisplayName("Create")
    class createComment {
        @DisplayName("댓글 등록 테스트")
        @Test
        void registerComment() {
            Board findBoard = boardRepository.findBoardByTitle("test1").get();
            Comment comment = new Comment("맛있는 레시피네요!", 0, 0, findBoard);
            findBoard.setComment(comment);
            Comment saveComment = commentRepository.save(comment);
            assertThat(saveComment).isEqualTo(comment);
        }
    }

    @Nested
    @DisplayName("Read")
    class findComment {
        @DisplayName("댓글 검색 테스트")
        @Test
        void findCommentByContent() {
            Board findBoard = boardRepository.findBoardByTitle("test1").get();
            Comment comment = new Comment("맛있는 레시피네요!", 0, 0, findBoard);
            findBoard.setComment(comment);
            commentRepository.save(comment);

//            List<Comment> findComment = commentRepository.findByContentLike("%레시피%");
            Comment findComment = commentRepository.findByBoardTitleAndContainingContent("test1","레시피").get();
            assertThat(findComment.getContent()).isEqualTo("맛있는 레시피네요!");
        }

        @Test
        @DisplayName("게시물에 작성된 \"모든\" 댓글 조회")
        void boardFindAll() {
            // given
            Long id = 1L;
            List<Comment> addComment = getComments();
            commentRepository.saveAll(addComment);

            // when
            List<Comment> comments = commentRepository.findAll();

            // then
            assertThat(addComment.size()).isEqualTo(comments.size());
        }
    }
    @Nested
    @DisplayName("Update")
    class updateCommentContent {
        @DisplayName("댓글 내용 수정 테스트")
        @Test
        void updateCommentContent() {
            Board findBoard = boardRepository.findBoardByTitle("test1").get();
            Comment comment = new Comment("맛있는 레시피네요!", 0, 0, findBoard);
            findBoard.setComment(comment);
            Comment saveComment = commentRepository.save(comment);

            String updateContent = "맛없는 레시피군요...컹스";
            saveComment.commentContentUpdate(updateContent);
            assertThat(comment.getContent()).isEqualTo(updateContent);
        }
    }

    @Nested
    @DisplayName("Delete")
    class deleteComment {
        @DisplayName("댓글 삭제 테스트")
        @Test
        void deleteCommentByContent() {
            Board findBoard = boardRepository.findBoardByTitle("test1").get();
            Comment comment = new Comment("맛있는 레시피네요!", 0, 0, findBoard);
            findBoard.setComment(comment);
            Comment saveComment = commentRepository.save(comment);

            commentRepository.delete(saveComment);
            Optional<Comment> deleteComment = commentRepository.findCommentById(saveComment.getId());
            assertThat(deleteComment.isPresent()).isTrue();
        }
    }
//    @AfterEach
//    void deleteAll() {
//        commentRepository.deleteAll();
//        boardRepository.deleteAll();
//        memberRepository.deleteAll();
//    }

    /**
     * 게시물을 담는 초기화 테스트 메서드
     *
     * @return
     */
    private List<Comment> getComments() {
        List<Comment> comments = new ArrayList<>();

        Board findBoardByTitle = boardRepository.findBoardByTitle(title.get(0)).get();

        Comment comment1 = new Comment("1빠", 0, 0, findBoardByTitle);
        Comment comment2 = new Comment("2빠", 0, 0, findBoardByTitle);
        Comment comment3 = new Comment("3빠", 0, 0, findBoardByTitle);

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        findBoardByTitle.setComment(comment1);
        findBoardByTitle.setComment(comment2);
        findBoardByTitle.setComment(comment3);

        return comments;
    }
}