package sejong.foodsns.controller.member.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.domain.member.Member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MemberReportControllerTest {

    @Autowired
    private MemberReportController memberReportController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static Member member;

    @BeforeAll
    static void init() {
        member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
    }

    @Nested
    @Order(0)
    @DisplayName("성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Success {

        @Test
        @Order(0)
        @DisplayName("신고 회원 등록")
        void reportMemberCreate() throws Exception{
            // given

            // when
            /*ResultActions resultActions = mockMvc.perform(post("/member/report")
                    .param("email", "swager253@naver.com")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());*/
        }
    }

    @Nested
    @Order(1)
    @DisplayName("실패")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Failed {

    }
}