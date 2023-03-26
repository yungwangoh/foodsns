package sejong.foodsns.controller.search;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegratedSearchControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @BeforeEach
    void init() {
        Member member = new Member("윤광오", "qwer1234@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member memberSave = memberRepository.save(member);

        Board board = new Board("김치찌개", "맛있다", 0L, 0, null, memberSave);
        Board boardSave = boardRepository.save(board);

        Comment comment = new Comment("맛있어요!", 0, 0, boardSave, memberSave);
        Comment commentSave = commentRepository.save(comment);

        Reply reply = new Reply("좋아요", 0, 0, commentSave, memberSave);
        replyRepository.save(reply);
    }

    @Test
    @Order(0)
    @DisplayName("게시물 통합 검색 API 테스트")
    void boardIntegratedSearchApiTest() {
        // given

        // when

        // then
    }

    @Test
    @Order(1)
    @DisplayName("댓글 통합 검색 API 테스트")
    void commentIntegratedSearchApiTest() {
        // given

        // when

        // then

    }

    @Test
    @Order(2)
    @DisplayName("대댓글 통합 검색 API 테스트")
    void replyIntegratedSearchApiTest() {
        // given

        // when

        // then

    }

    @Test
    @Order(3)
    @DisplayName("통합 검색 API 테스트")
    void integratedSearchApiTest() {
        // given

        // when

        // then

    }

    @AfterEach
    void dbInit() {
        replyRepository.deleteAll();
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }
}