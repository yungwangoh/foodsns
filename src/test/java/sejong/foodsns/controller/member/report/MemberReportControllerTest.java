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
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MemberReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReportMemberRepository reportMemberRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @Order(0)
    @DisplayName("성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Success {

        @Test
        @Order(0)
        @DisplayName("신고 회원 등록 -> ACCEPT")
        void reportMemberCreateAccept() throws Exception{
            // given
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            member.reportCount();

            memberRepository.save(member);

            String name = "email";
            String values = "swager253@dfdgfdg.com";

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/report")
                    .param(name, values)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            // then -> 신고 수가 하나 이므로 ACCEPT
            resultActions.andExpect(status().isAccepted())
                    .andDo(print());
        }

        @Test
        @Order(1)
        @DisplayName("신고 회원 등록 -> CREATED")
        void reportMemberCreateCreated() throws Exception{
            // given
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            for(int i = 0; i < 10; i++) member.reportCount();

            memberRepository.save(member);

            String name = "email";
            String values = "swager253@dfdgfdg.com";

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/report")
                    .param(name, values)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @Order(2)
        @DisplayName("신고 회원 목록 -> OK")
        void reportMemberListOk() throws Exception{
            // given -> 회원은 신고 수를 10개 받고 신고 레포에 저장되어있다고 가정.
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            for(int i = 0; i < 10; i++) member.reportCount();

            Member save = memberRepository.save(member);
            reportMemberRepository.save(new ReportMember(save));

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/reports"));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @Order(3)
        @DisplayName("신고 회원 조회 -> OK")
        void reportMemberSearchOk() throws Exception {
            // given -> 회원은 신고 수를 10개 받고 신고 레포에 저장되어있다고 가정.
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            for(int i = 0; i < 10; i++) member.reportCount();

            Member save = memberRepository.save(member);
            ReportMember reportMember = reportMemberRepository.save(new ReportMember(save));

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/report/{id}", reportMember.getId()));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }

        @AfterEach
        void initDB() {
            reportMemberRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }

    @Nested
    @Order(1)
    @DisplayName("실패")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Failed {

        @Test
        @Order(0)
        @DisplayName("신고 등록 할 회원을 찾을 수 없을 때 실패 -> NOT_FOUND")
        void reportMemberNotSearchMemberNotFound() throws Exception{
            // given -> 회원은 신고 수를 10개 받고 신고 레포에 저장되어있다고 가정.
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            member.reportCount();

            memberRepository.save(member);

            String name = "email";
            String values = "swager253@kakao.com";

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/report")
                    .param(name, values)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @Order(1)
        @DisplayName("신고 리스트에 회원이 없을 경우 -> 잘못된 id 파라미터")
        void reportMemberListNotMemberWrongIdNotFound() throws Exception {
            // given -> 회원은 신고 수를 10개 받고 신고 레포에 저장되어있다고 가정.
            Member member = new Member("윤광오", "swager253@dfdgfdg.com", "gfjkdsfd@3", NORMAL);
            for(int i = 0; i < 10; i++) member.reportCount();

            Member save = memberRepository.save(member);
            reportMemberRepository.save(new ReportMember(save));

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/report/{id}", 0));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @AfterEach
        void initDB() {
            reportMemberRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }
}