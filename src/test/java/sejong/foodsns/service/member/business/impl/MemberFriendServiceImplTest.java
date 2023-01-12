package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;
import sejong.foodsns.service.member.business.MemberFriendService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberFriendServiceImplTest {

    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private MemberBlackListService memberBlackListService;
    @Autowired
    private MemberRepository memberRepository;
    private MemberRequestDto memberRequestDto;
    private MemberRequestDto memberRequestDto1;
    private MemberRequestDto memberRequestDto2;

    @BeforeEach
    void init() {
        memberRequestDto = MemberRequestDto.builder()
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .username("윤광오")
                .build();

        memberRequestDto1 = MemberRequestDto.builder()
                .email("qkfks1234@naver.com")
                .password("rhkddh77@A")
                .username("하윤")
                .build();

        memberRequestDto2 = MemberRequestDto.builder()
                .email("ssapper234@naver.com")
                .password("rhkddh77@A")
                .username("임우택")
                .build();
    }

    @Test
    @Order(0)
    @DisplayName("친구 추가 성공")
    void myFriendAddingServiceSuccess() {
        // given
        String testUserName = "하윤";
        String testUserName1 = "임우택";
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);

        // when
        memberFriendService.friendMemberAdd(memberRequestDto, testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto, testUserName1);

        ResponseEntity<List<MemberResponseDto>> memberList = memberFriendService.friendMemberList(memberRequestDto);

        // then -> 친구 2명 저장 -> 기댓값 2
        assertThat(memberList.getBody().size()).isEqualTo(2);
        assertThat(memberList.getBody().get(0).getUsername()).isEqualTo(testUserName);
        assertThat(memberList.getBody().get(1).getUsername()).isEqualTo(testUserName1);
    }
}