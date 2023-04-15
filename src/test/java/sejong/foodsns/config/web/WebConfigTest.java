package sejong.foodsns.config.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.member.login.jwt.MemberLoginService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private BoardCrudService boardCrudService;

    private final MemberRequestDto requestDto = MemberRequestDto.builder()
            .username("윤광오")
            .password("rhkddh77@A")
            .email("swager253@naver.com")
            .build();

    private MemberLoginDto loginDto;

    @BeforeEach
    void MemberInit() {
        loginDto = new MemberLoginDto("swager253@naver.com", "rhkddh77@A");
        memberCrudService.memberCreate(requestDto);
    }

    @Test
    @DisplayName("인터셉터 테스트 -> member uri 관련은 접근 가능 다른 uri는 접근 불가능 테스트")
    void memberUriIsAccessibleOtherUriIsNotAccessible() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(post("/board"));

        // then
        resultActions.andExpect(status().isFound())
                .andDo(print());
    }

    @Test
    @DisplayName("인터셉터 테스트 -> 로그인 안해도 member uri 접근 가능 테스트")
    void noLoginHoweverMemberUriAccess() throws Exception {
        // given
        String email = "swager253@naver.com";

        // when
        ResultActions resultActions = mockMvc.perform(get("/member/{email}", email));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("인터셉터 테스트 -> 로그인 하면 board uri 접근 가능")
    void loginBoardUriIsAccessible() throws Exception {
        // given
        memberLoginService.jwtLogin(loginDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/boards"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}