package sejong.foodsns.controller.board.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentRequestDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.CommentCrudService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CommentCrudService commentCrudService;
    @Autowired private BoardCrudService boardCrudService;
    @Autowired private MemberRepository memberRepository;

    private MemberRequestDto memberRequestDto;
    private BoardRequestDto boardRequestDto;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    public void initComment() throws IOException {
        Member postMember = new Member("윤광오", "swager253@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member savePostMember = memberRepository.save(postMember);

        Member commentMember = new Member("하윤", "gkdbssla97@naver.com", "asdf1997@BA", MemberType.NORMAL);
        Member saveCommentMember = memberRepository.save(commentMember);

        String name = "image";
        String originalFileName = "test.jpeg";
        String contentType = "image/jpeg";
        String fileUrl = "/Users/yungwang-o/Documents/board_file";

        List<MultipartFile> mockMultipartFiles = new ArrayList<>();
        mockMultipartFiles.add(new MockMultipartFile(name, originalFileName, contentType, new FileInputStream(fileUrl)));

        boardRequestDto = BoardRequestDto.builder()
                .title("자취 레시피 공유합니다.")
                .username(savePostMember.getUsername())
                .content("김치찌개 레시피 1....")
                .build();

        ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, mockMultipartFiles);

        commentRequestDto = CommentRequestDto.builder()
                .content("돈까스 맛있네요!")
                .email(saveCommentMember.getEmail())
                .boardId(getBoardResponseDto(boardCreate).getId())
                .build();
    }

    @Test
    @Order(0)
    @DisplayName("댓글 등록 컨트롤러")
    void registerComment() throws Exception {
        String s = objectMapper.writeValueAsString(commentRequestDto);
        ResultActions resultActions = mockMvc.perform(post("/comment")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @Order(1)
    @DisplayName("전체 댓글 조회")
    void findAllComments() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/comments"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("멤버이름으로 댓글 목록 조회 OK")
    void findAllCommentsByUsername() throws Exception {
        // given
        commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());
        String username = "하윤";

        // when
        ResultActions resultActions = mockMvc.perform(get("/comment/{username}", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("게시물 제목으로 댓글 목록 조회 OK")
    void boardSearchOK() throws Exception {
        // given
        commentCrudService.commentCreate(commentRequestDto.getContent(), commentRequestDto.getBoardId(), commentRequestDto.getEmail());
        String username = "하윤";

        // when
        ResultActions resultActions = mockMvc.perform(get("/comment/{username}", username));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    private static BoardResponseDto getBoardResponseDto(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return boardCreate.getBody().get();
    }
}
