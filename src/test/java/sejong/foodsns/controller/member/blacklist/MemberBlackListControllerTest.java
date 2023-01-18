package sejong.foodsns.controller.member.blacklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MemberBlackListControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private MemberBusinessService memberBusinessService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BlackListRepository blackListRepository;

    @Nested
    @Order(0)
    @DisplayName("성공")
      class Success {

        @Test
        @DisplayName("블랙리스트 등록 API 테스트 성공")
        void blackListMemberCreateSuccess() throws Exception {
            // given
            MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                    .email("swager253@zzz.com")
                    .password("qwer1234@A")
                    .username("윤광오")
                    .build();

            blackListInit(memberRequestDto, 30);

            MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = MemberBlackListCreateRequestDto.builder()
                    .reason("악의적인 댓글")
                    .memberRequestDto(memberRequestDto)
                    .build();

            String blackListCreateDto = objectMapper.writeValueAsString(memberBlackListCreateRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/member/blackList")
                    .content(blackListCreateDto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName("블랙리스트 회원 (사유) 조회 API 테스트")
        void blackListMemberReasonSearch() throws Exception {
            // given
            MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                    .email("swager253@zzz.com")
                    .password("qwer1234@A")
                    .username("윤광오")
                    .build();

            // 블랙리스트 등록하기 위한 초기화
            blackListInit(memberRequestDto, 30);

            MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = MemberBlackListCreateRequestDto.builder()
                    .reason("악의적인 댓글")
                    .memberRequestDto(memberRequestDto)
                    .build();

            String blackListCreateDto = objectMapper.writeValueAsString(memberBlackListCreateRequestDto);

            MvcResult mvcResult = mockMvc.perform(post("/member/blackList")
                            .content(blackListCreateDto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            MemberBlackListResponseDto memberBlackListResponseDto = getMemberBlackListResponseDto(mvcResult);

            // when
            ResultActions resultActions =
                    mockMvc.perform(get("/member/blackList/{id}", memberBlackListResponseDto.getId()));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("블랙리스트 회원의 상세 조회 API 테스트")
        void blackListMemberDetailSearch() throws Exception {
            // given
            MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                    .email("swager253@zzz.com")
                    .password("qwer1234@A")
                    .username("윤광오")
                    .build();

            // 블랙리스트 등록하기 위한 초기화
            blackListInit(memberRequestDto, 30);

            MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = MemberBlackListCreateRequestDto.builder()
                    .reason("악의적인 댓글")
                    .memberRequestDto(memberRequestDto)
                    .build();

            String blackListCreateDto = objectMapper.writeValueAsString(memberBlackListCreateRequestDto);

            MvcResult mvcResult = mockMvc.perform(post("/member/blackList")
                            .content(blackListCreateDto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            MemberBlackListResponseDto memberBlackListResponseDto = getMemberBlackListResponseDto(mvcResult);

            // when
            ResultActions resultActions =
                    mockMvc.perform(get("/member/blackList/search/{id}", memberBlackListResponseDto.getId()));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @Order(1)
    @DisplayName("실패")
    class Failed {

        @Test
        @DisplayName("URI 잘못 적었을 시에 실패 API 테스트 -> METHOD_NOT_ALLOWED")
        void uriMissMatchNotFound() throws Exception {
            // given
            MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                    .email("swager253@zzz.com")
                    .password("qwer1234@A")
                    .username("윤광오")
                    .build();

            // 블랙리스트 등록하기 위한 초기화
            blackListInit(memberRequestDto, 30);

            MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = MemberBlackListCreateRequestDto.builder()
                    .reason("악의적인 댓글")
                    .memberRequestDto(memberRequestDto)
                    .build();

            String blackListCreateDto = objectMapper.writeValueAsString(memberBlackListCreateRequestDto);

            // when -> "/member/blackList" 에서 철자 L -> l 이 틀림.
            ResultActions resultActions = mockMvc.perform(post("/member/blacklist")
                    .content(blackListCreateDto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

            // then -> 기댓값 405
            resultActions.andExpect(status().isMethodNotAllowed())
                    .andDo(print());
        }

        @Test
        @DisplayName("찾고자 하는 블랙리스트 회원이 없을시 API 테스트 -> NOT_FOUND")
        void searchNotExistBlackListMember() throws Exception {
            // given
            MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                    .email("swager253@zzz.com")
                    .password("qwer1234@A")
                    .username("윤광오")
                    .build();

            // 일반 유저 -> 블랙리스트 등록이 불가능하다. 30개가 넘지 않은 신고 수.
            blackListInit(memberRequestDto, 30);

            MemberBlackListCreateRequestDto memberBlackListCreateRequestDto = MemberBlackListCreateRequestDto.builder()
                    .reason("악의적인 댓글")
                    .memberRequestDto(memberRequestDto)
                    .build();

            String blackListCreateDto = objectMapper.writeValueAsString(memberBlackListCreateRequestDto);

            mockMvc.perform(post("/member/blackList")
                            .content(blackListCreateDto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON));

            // when -> 잘못된 id
            ResultActions resultActions = mockMvc.perform(get("/member/blackList/{id}", 3));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @AfterEach
    void initDB() {
        blackListRepository.deleteAll();
        memberRepository.deleteAll();
    }

    private MemberResponseDto getMemberResponseDto(ResponseEntity<Optional<MemberResponseDto>> init) {
        return init.getBody().get();
    }

    private MemberBlackListResponseDto getMemberBlackListResponseDto(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, MemberBlackListResponseDto.class);
    }

    private void blackListInit(MemberRequestDto memberRequestDto, int num) {
        memberCrudService.memberCreate(memberRequestDto);
        for(int i = 0; i < num; i++) memberBusinessService.memberReportCount(memberRequestDto.getEmail());
        memberBusinessService.memberBlackListTypeConvert(memberRequestDto.getEmail());
    }
}