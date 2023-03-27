package sejong.foodsns.controller.search;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.CommentCrudService;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegratedSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    private Member memberSave;

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
    void boardIntegratedSearchApiTest() throws Exception {
        // given
        String username = "윤";
        String content = "김";

        // when
        ResultActions resultActions = mockMvc.perform(get("/integration/search/board")
                .param("content", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(1)
    @DisplayName("댓글 통합 검색 API 테스트")
    void commentIntegratedSearchApiTest() throws Exception {
        // given
        String username = "윤";
        String content = "김";

        // when
        ResultActions resultActions = mockMvc.perform(get("/integration/search/comment")
                .param("content", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("대댓글 통합 검색 API 테스트")
    void replyIntegratedSearchApiTest() throws Exception {
        // given
        String username = "윤";
        String content = "김";

        // when
        ResultActions resultActions = mockMvc.perform(get("/integration/search/reply")
                .param("content", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("통합 검색 API 테스트")
    void integratedSearchApiTest() throws Exception {
        // given
        String username = "윤";
        String content = "김";

        // when
        ResultActions resultActions = mockMvc.perform(get("/integration/search")
                .param("content", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @AfterEach
    void dbInit() {
        replyRepository.deleteAll();
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }
}