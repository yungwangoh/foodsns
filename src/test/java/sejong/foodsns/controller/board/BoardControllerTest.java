package sejong.foodsns.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.update.BoardUpdateTitleDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.update.MemberUpdateUserNameDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.member.crud.MemberCrudService;

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
    public void initBoard() {
        Member member = new Member("윤광오", "swager253@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member saveMember = memberRepository.save(member);

        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();

        boardRequestDto = BoardRequestDto.builder()
                .title("test1")
                .content("hi")
                .memberRequestDto(new MemberRequestDto(saveMember.getUsername(),
                        saveMember.getEmail(), saveMember.getPassword()))
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

        String s = objectMapper.writeValueAsString(boardRequestDto);
        ResultActions resultActions = mockMvc.perform(post("/board")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("result : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    /**
     * 게시물 제목 중복으로 인한 등록 실패
     * @return 게시물, BADREQUEST
     */
    @Test
    @Order(1)
    @DisplayName("게시물 제목중복으로 인한 등록실패 컨트롤러")
    void registerBoardBadRequest() throws Exception {

        String save = objectMapper.writeValueAsString(boardRequestDto);
        MvcResult mvcResult = mockMvc.perform(post("/board")
                        .content(save)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("result : " + mvcResult.getResponse().getContentAsString());

        BoardRequestDto boardRequestDtoTmp = BoardRequestDto.builder()
                .title("test1")
                .content("anything you can cook recipe")
                .memberRequestDto(memberRequestDto)
                .build();

        String s = objectMapper.writeValueAsString(boardRequestDtoTmp);
        ResultActions resultActions = mockMvc.perform(post("/board")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("게시물 찾기 OK")
    void boardSearchOK() throws Exception {
        // given
        boardCrudService.boardCreate(boardRequestDto);
        String title = "test1";

        // when
        ResultActions resultActions = mockMvc.perform(get("/board/{title}", title));

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
    @DisplayName("게시물 제목 수정 실패")
    void memberNameUpdateFailed() throws Exception {
        // given
        boardCrudService.boardCreate(boardRequestDto);

        BoardUpdateTitleDto boardUpdateTitleDto = BoardUpdateTitleDto.builder()
                .updateTitle("test2sadjfklasjfkl;dsjfkl;adjsfkl;jdsaklf;ajsdkl;fjsdaklfjsdalkfjasdkl;fjdaskl;jfkalds;fjdkls;ajfa")
                .orderTitle("test1")
                .build();

        String updateTitle = objectMapper.writeValueAsString(boardUpdateTitleDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/board/title")
                .content(updateTitle)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotModified())
                .andDo(print());
    }
    @Test
    @Order(5)
    @DisplayName("게시물 제목 수정 성공 OK")
    void memberNameUpdate() throws Exception {
        // given
        boardCrudService.boardCreate(boardRequestDto);

        BoardUpdateTitleDto boardUpdateTitleDto = BoardUpdateTitleDto.builder()
                .updateTitle("test2")
                .orderTitle("test1")
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
        boardCrudService.boardCreate(boardRequestDto);
        String content = "test";
        String content1 = "hi";

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/board/{search-option}/{content}", SearchOption.ALL, content));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @AfterEach
    void deleteAll() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
