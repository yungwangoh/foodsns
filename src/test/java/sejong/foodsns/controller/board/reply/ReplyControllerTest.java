package sejong.foodsns.controller.board.reply;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.reply.ReplyRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.member.MemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    private Board saveBoard;
    private Comment saveComment;

    @BeforeEach
    void init() {

        Member member = new Member("윤광오", "qwer1234@naver.com", "qwer1234@A", NORMAL);
        saveMember = memberRepository.save(member);

        Board board = new Board("좋아", "좋아", 0L, 0, null, saveMember);
        saveBoard = boardRepository.save(board);

        Comment comment = new Comment("ㅎㅇ", 0, 0, saveBoard, saveMember);
        saveComment = commentRepository.save(comment);
    }

    @Test
    @Order(0)
    @DisplayName("대댓글 등록")
    void replyCreate() throws Exception {
        // given
        String content = "좋아";
        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());
        String s = objectMapper.writeValueAsString(replyRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/reply")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }
}