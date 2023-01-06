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
import sejong.foodsns.dto.member.update.MemberUpdateUserNameDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void init() {
        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
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
        ResultActions resultActions = mockMvc.perform(post("/member")
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
        ResultActions resultActions = mockMvc.perform(post("/member")
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
    //@Disabled
    void registerMemberNotFound() throws Exception {
        // given
        String memberCreate = objectMapper.writeValueAsString(memberRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/member")
                .content(memberCreate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("회원 찾기 OK")
    void memberSearchOK() throws Exception {
        // given
        String email = "swager253@naver.com";

        // when
        ResultActions resultActions = mockMvc.perform(get("/member/{email}", email));

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

    @Test
    @Order(5)
    @DisplayName("회원 이름 수정 OK")
    void memberNameUpdate() throws Exception {
        // given
        MemberUpdateUserNameDto updateUserNameDto = MemberUpdateUserNameDto.builder()
                .email("swager253@naver.com")
                .username("윤재경")
                .build();

        String updateName = objectMapper.writeValueAsString(updateUserNameDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/member/username")
                .content(updateName)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(10)
    @DisplayName("회원 탈퇴 NO_CONTENT")
    void memberDeleteNoContent() throws Exception{
        // given
        String memberDelete = objectMapper.writeValueAsString(memberRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/member")
                .content(memberDelete)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
    }
}