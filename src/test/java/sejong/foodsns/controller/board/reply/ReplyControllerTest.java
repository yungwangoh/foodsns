package sejong.foodsns.controller.board.reply;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.reply.ReplyRequestDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.reply.ReplyUpdateDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    private ReplyCrudService replyCrudService;

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

    @Test
    @Order(1)
    @DisplayName("대댓글 유저 이름으로 검색")
    void userNameReplySearch() throws Exception {
        // given
        String username = "윤광오";
        String content = "좋아";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());
        replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(get("/replies/username")
                .param("username", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("대댓글 내용으로 검색")
    void contentReplySearch() throws Exception {
        // given
        String content = "좋아";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());
        replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(get("/replies/content")
                .param("content", content));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("대댓글 수정 테스트")
    void replyUpdateTest() throws Exception {
        // given
        String content = "좋아";
        String updateContent = "ㅡㅡ";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());

        ResponseEntity<Optional<ReplyResponseDto>> replyCreate =
                replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto(getReplyResponseDto(replyCreate).getId(), updateContent);
        String s = objectMapper.writeValueAsString(replyUpdateDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/reply")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("대댓글 찾기 id")
    void replySearchByReplyId() throws Exception {
        // given
        String content = "좋아";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());
        ResponseEntity<Optional<ReplyResponseDto>> replyCreate =
                replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(get("/reply/{replyId}", getReplyResponseDto(replyCreate).getId()));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(5)
    @DisplayName("대댓글 삭제 테스트")
    void replyDeleteTest() throws Exception {
        // given
        String content = "좋아";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, saveComment.getId(), saveMember.getEmail());
        ResponseEntity<Optional<ReplyResponseDto>> replyCreate =
                replyCrudService.replyCreate(replyRequestDto.getContent(), replyRequestDto.getCommentId(), replyRequestDto.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(delete("/reply/{replyId}", getReplyResponseDto(replyCreate).getId()));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
    }

    private static ReplyResponseDto getReplyResponseDto(ResponseEntity<Optional<ReplyResponseDto>> replyCreate) {
        return replyCreate.getBody().get();
    }
}