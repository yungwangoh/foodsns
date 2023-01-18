package sejong.foodsns.controller.member.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.jwt.JwtProvider;
import sejong.foodsns.service.member.crud.MemberCrudService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MemberLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;
    private static MvcResult mvcResult;

    @BeforeEach
    void init() {
        MemberRequestDto memberRequestDto = getMemberRequestDto("swager253@naver.com",
                "윤광오", "rhkddh77@A");

        memberCrudService.memberCreate(memberRequestDto);
    }

    @AfterEach
    void initDB() {
        memberCrudService.memberDelete(getMemberRequestDto("swager253@naver.com", "윤광오", "rhkddh77@A"));
    }

    @Nested
    @DisplayName("성공")
    @Order(0)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Success {

        @Test
        @Order(0)
        @DisplayName("로그인 성공")
        void loginSuccess() throws Exception {
            // given
            MemberLoginDto memberLoginDto = getMemberLoginDto("swager253@naver.com", "rhkddh77@A");
            String memberLogin = objectMapper.writeValueAsString(memberLoginDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/login")
                    .content(memberLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            mvcResult = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
        }

        @Test
        @Order(1)
        @DisplayName("로그아웃 성공")
        void logoutSuccess() throws Exception {
            // given
            String email = "swager253@naver.com";
            String token = mvcResult.getResponse().getContentAsString();

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/logout")
                    .param("email", email)
                    .header("X-AUTH-TOKEN", token));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("실패")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Failed {

        @BeforeEach
        void initLogin() throws Exception {
            MemberLoginDto memberLoginDto = getMemberLoginDto("swager253@naver.com", "rhkddh77@A");
            String memberLogin = objectMapper.writeValueAsString(memberLoginDto);

            mockMvc.perform(post("/member/login")
                    .content(memberLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        @Order(2)
        @DisplayName("로그인 실패 -> 잘못된 이메일 로그인")
        void loginFailedWrongEmail() throws Exception {
            // given
            MemberLoginDto memberLoginDto = getMemberLoginDto("qkfks1234@naver.com", "rhkddh77@A");
            String memberLogin = objectMapper.writeValueAsString(memberLoginDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/login")
                    .content(memberLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @Order(3)
        @DisplayName("로그인 실패 -> 잘못된 비밀번호")
        void loginFailedWrongPassword() throws Exception {
            // given
            MemberLoginDto memberLoginDto = getMemberLoginDto("swager253@naver.com", "alstngud77@A");
            String memberLogin = objectMapper.writeValueAsString(memberLoginDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/login")
                    .content(memberLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @Order(4)
        @DisplayName("로그인 실패 -> 잘못된 형식 유효성 검사 (이메일, 비밀번호)")
        void loginFailedWrongSyntax() throws Exception {
            // given
            MemberLoginDto memberLoginDto = getMemberLoginDto("swager253", "alstngud77@A");
            String memberLogin = objectMapper.writeValueAsString(memberLoginDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/login")
                    .content(memberLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());

        }

        @Test
        @Order(6)
        @DisplayName("로그아웃 실패 -> 유효하지 않는 토큰")
        void logoutFailedTokenIsNotValidated() throws Exception {
            // given
            String email = "swager253@naver.com";
            String accessToken = jwtProvider.createToken(email, 0L);

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/logout")
                    .param("email", email)
                    .header("X-AUTH-TOKEN", accessToken));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    private MemberRequestDto getMemberRequestDto(String email, String username, String pwd) {
        return MemberRequestDto.builder()
                .email(email)
                .password(pwd)
                .username(username)
                .build();
    }

    private MemberLoginDto getMemberLoginDto(String email, String pwd) {
        return MemberLoginDto.builder()
                .email(email)
                .password(pwd)
                .build();
    }
}