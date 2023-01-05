package sejong.foodsns.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.service.member.crud.MemberCrudService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberCrudService memberCrudService;
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void init() {
        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swaegr253@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    @Test
    @Order(0)
    @DisplayName("회원 가입 OK")
    void registerMemberOk() throws Exception {
        // given
        String memberCreate = objectMapper.writeValueAsString(memberRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/member/create")
                        .content(memberCreate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @Order(1)
    @DisplayName("회원 가입 (Bad Request -> Validation.)")
    void registerMemberBadRequest() throws Exception {
        // given
        MemberRequestDto memberRequestDto1 = MemberRequestDto.builder()
                .username("윤")
                .email("swager253")
                .password("rhkddh77@")
                .build();

        String memberCreate = objectMapper.writeValueAsString(memberRequestDto1);

        // when
        ResultActions resultActions = mockMvc.perform(post("/member/create")
                .content(memberCreate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("회원 가입 (NOT_FOUND -> 중복 회원 가입)")
    @Disabled
    void registerMemberNotFound() throws Exception{
        // given
        String memberCreate = objectMapper.writeValueAsString(memberRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/member/create")
                .content(memberCreate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("회원 찾기 OK")
    //@Disabled
    void memberSearchOK() throws Exception {
        // given
        String email = "swager253@naver.com";

        // when
        ResultActions resultActions = mockMvc.perform(get("/member/search/{email}", email));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("회원 목록 조회 OK")
    void memberList() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/members"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}