package sejong.foodsns.controller.member.friend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberFriendService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sejong.foodsns.domain.member.MemberType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MemberFriendControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;

    @AfterEach
    void initDB() {
        friendRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    @Order(0)
    @DisplayName("성공")
    class Success {

        @Test
        @Order(0)
        @DisplayName("친구 등록 -> CREATED")
        void myFriendCreateCreated() throws Exception {
            // given
            String name = "email";
            String values = "swager253@zzz.com";

            String name1 = "friendUsername";
            String values1 = "하윤";

            Member member = new Member("윤광오", "swager253@zzz.com", "zlzlzl123@", NORMAL);
            Member friend = new Member("하윤", "qkfks1234@zzz.com", "zlzlzl123@", NORMAL);
            memberRepository.save(member);
            memberRepository.save(friend);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/friend")
                    .param(name, values)
                    .param(name1, values1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @Order(1)
        @DisplayName("친구 목록 -> OK")
        void myFriendListOk() throws Exception {
            // given
            String name = "email";
            String values = "swager253@zzz.com";

            Member member = new Member("윤광오", "swager253@zzz.com", "zlzlzl123@", NORMAL);
            Member friend = new Member("하윤", "qkfks1234@zzz.com", "zlzlzl123@", NORMAL);
            memberRepository.save(member);
            memberRepository.save(friend);

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/friends")
                    .param(name, values));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @Order(2)
        @DisplayName("친구 찾기 -> OK")
        void myFriendSearchOk() throws Exception {
            // given
            String name = "email";
            String values = "swager253@zzz.com";

            String name1 = "friendUsername";
            String values1 = "하윤";

            String index = "index";

            Member member = new Member("윤광오", "swager253@zzz.com", "zlzlzl123@", NORMAL);
            Member friend = new Member("하윤", "qkfks1234@zzz.com", "zlzlzl123@", NORMAL);
            memberRepository.save(member);
            memberRepository.save(friend);

            mockMvc.perform(post("/member/friend")
                    .param(name, values)
                    .param(name1, values1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // when
            ResultActions resultActions = mockMvc.perform(get("/member/friend")
                    .param(name, values)
                    .param(index, String.valueOf(0)));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }

        @AfterEach
        void initDB() {
            friendRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }

    @Nested
    @Order(1)
    @DisplayName("실패")
    class Failed {

        @Test
        @Order(0)
        @DisplayName("같은 친구를 추가하려할떄 실패 -> NOT_FOUND")
        void myFriendDuplicatedNotFound() {

        }

        @Test
        @Order(1)
        @DisplayName("회원 자신을 친구 추가 하려할 때 -> NOT_FOUND")
        void mySelfFriendListAddNotFound() {

        }
    }
}