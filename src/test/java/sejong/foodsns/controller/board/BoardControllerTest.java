package sejong.foodsns.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.crud.MemberCrudService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberCrudService memberCrudService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;

    private BoardRequestDto boardRequestDto;
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    public void initBoard() {
        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();

        Member member = new Member("윤광오", "swager253@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member save = memberRepository.save(member);

        boardRequestDto = BoardRequestDto.builder()
                .title("test1")
                .content("hi")
                .memberRequestDto(memberRequestDto)
                .build();
    }

    /**
     * 게시물 등록
     * @return 게시물, CREATE
     */
    @Test
    void registerBoard() throws Exception {

        String s = objectMapper.writeValueAsString(boardRequestDto);
        ResultActions resultActions = mockMvc.perform(post("/board")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        Board board = boardRepository.findBoardByTitle("test1").get();
        System.out.println(board.getTitle());
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("result : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isCreated())
                .andDo(print());
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    /**
     * 게시물 등록 실패
     * @return 게시물, BADREQUEST
     */
    @Test
    void registerBoardBadRequest() throws Exception {

//        memberRepository.save(memberRequestDto.toEntity());
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

//        resultActions.andExpect(status().isCreated())
//                .andDo(print());
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @AfterEach
    void deleteAll() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
