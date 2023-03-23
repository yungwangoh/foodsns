package sejong.foodsns.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.board.delete.BoardDeleteDto;
import sejong.foodsns.dto.board.update.BoardUpdateTitleDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.update.MemberUpdateUserNameDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardCrudService boardCrudService;

    private BoardRequestDto boardRequestDto;
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void initBoard() {
        Member member = new Member("윤광오", "swager253@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member saveMember = memberRepository.save(member);

        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("qwer1234@A")
                .build();

        boardRequestDto = BoardRequestDto.builder()
                .title("test1")
                .content("hi")
                .username(saveMember.getUsername())
                .build();
    }

    /**
     * 게시물 등록
     * @return 게시물, CREATE
     */
    @Test
    @Order(0)
    @DisplayName("게시물 등록 컨트롤러")
    void registerBoard() throws Exception {

        String fileUrl = "/Users/yungwang-o/Documents/test.jpeg";
        String s = objectMapper.writeValueAsString(boardRequestDto);

        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("board", "board", "application/json", s.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile request =
                new MockMultipartFile("image", "test.jpeg", "image/jpeg", new FileInputStream(fileUrl));

        ResultActions resultActions = mockMvc.perform(multipart("/board")
                .file(mockMultipartFile)
                .file(request));

        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("result : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("게시물 찾기 OK")
    void boardSearchOK() throws Exception {
        // given
        List<MultipartFile> mockMultipartFiles = new ArrayList<>();

        ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, mockMultipartFiles);

        // when
        ResultActions resultActions = mockMvc.perform(get("/board/{id}", getBoardResponseDto(boardCreate).getId()));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("전체 게시물 조회")
    void findAllBoards() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/boards"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("게시물 제목 수정 성공")
    void memberNameUpdateFailed() throws Exception {
        // given
        String username = "윤광오";
        String name = "image";
        String originalFileName = "test.jpeg";
        String contentType = "image/jpeg";
        String fileUrl = "/Users/yungwang-o/Documents/test.jpeg";

        List<MultipartFile> mockMultipartFiles = new ArrayList<>();
        //mockMultipartFiles.add(new MockMultipartFile(name, originalFileName, contentType, new FileInputStream(fileUrl)));

        ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, mockMultipartFiles);

        BoardUpdateTitleDto boardUpdateTitleDto = BoardUpdateTitleDto.builder()
                .id(getBoardResponseDto(boardCreate).getId())
                .username(username)
                .updateTitle("하이용!!")
                .build();

        String updateTitle = objectMapper.writeValueAsString(boardUpdateTitleDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/board/title")
                .content(updateTitle)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("검색 옵션을 통한 게시물 API 테스트")
    void searchOptionBoardApiTest() throws Exception{
        // given
        List<MultipartFile> mockMultipartFiles = new ArrayList<>();

        boardCrudService.boardCreate(boardRequestDto, mockMultipartFiles);
        String content = "test";
        String content1 = "hi";

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/board/{search-option}/{content}", SearchOption.ALL, content1));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제 성공")
    void boardDeleteControllerOK() throws Exception {
        // given
        List<MultipartFile> multipartFiles = new ArrayList<>();

        ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

        BoardDeleteDto boardDeleteDto = new BoardDeleteDto(getBoardResponseDto(boardCreate).getId(), "윤광오");
        String s = objectMapper.writeValueAsString(boardDeleteDto);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/board")
                .content(s)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
    }

    @AfterEach
    void deleteAll() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    private static BoardResponseDto getBoardResponseDto(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return boardCreate.getBody().get();
    }
}
