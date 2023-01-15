package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
import sejong.foodsns.service.member.business.MemberFriendService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MemberFriendServiceImplTest {

    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private MemberCrudService memberCrudService;

    private static MemberRequestDto memberRequestDto;
    private static MemberRequestDto memberRequestDto1;
    private static MemberRequestDto memberRequestDto2;

    private static String testUserName;
    private static String testUserName1;

    @BeforeAll
    static void init() {
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

        testUserName = "하윤";
        testUserName1 = "임우택";
    }

    @AfterEach
    void initDB() {

    }

    @Test
    @Order(0)
    @DisplayName("친구 추가 성공")
    void myFriendAddingServiceSuccess() {
        // given
        myFriendAddInit();

        // when
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName1);

        ResponseEntity<List<MemberFriendResponseDto>> memberList = memberFriendService.friendMemberList(memberRequestDto.getEmail());

        // then -> 친구 2명 저장 -> 기댓값 2
        assertThat(memberList.getBody().size()).isEqualTo(2);
        assertThat(memberList.getBody().get(0).getUsername()).isEqualTo(testUserName);
        assertThat(memberList.getBody().get(1).getUsername()).isEqualTo(testUserName1);
    }

    @Test
    @Order(1)
    @DisplayName("친구 상세 조회 성공")
    void myFriendSearchSuccess() {
        // given
        myFriendAddInit();
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName1);

        ResponseEntity<List<MemberFriendResponseDto>> friendMemberList = memberFriendService.friendMemberList(memberRequestDto.getEmail());

        // when
        ResponseEntity<MemberResponseDto> memberDetailSearch =
                memberFriendService.friendMemberDetailSearch(memberRequestDto.getEmail(), 0);

        ResponseEntity<MemberResponseDto> memberDetailSearch1 =
                memberFriendService.friendMemberDetailSearch(memberRequestDto.getEmail(), 1);

        // then
        assertThat(friendMemberList.getBody().get(0).getUsername()).isEqualTo(memberDetailSearch.getBody().getUsername());
        assertThat(friendMemberList.getBody().get(0).getUsername()).isEqualTo(memberDetailSearch.getBody().getUsername());

        assertThat(friendMemberList.getBody().get(1).getUsername()).isEqualTo(memberDetailSearch1.getBody().getUsername());
        assertThat(friendMemberList.getBody().get(1).getUsername()).isEqualTo(memberDetailSearch1.getBody().getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("친구 삭제 성공")
    void myFriendDeleteSuccess() {
        // given
        myFriendAddInit();
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName1);

        // when
        ResponseEntity<MemberResponseDto> friendMemberDelete =
                memberFriendService.friendMemberDelete(memberRequestDto.getEmail(), 1);

        ResponseEntity<List<MemberFriendResponseDto>> friendMemberList = memberFriendService.
                friendMemberList(memberRequestDto.getEmail());

        // then
        assertThat(friendMemberList.getBody().size()).isEqualTo(1);
        assertThat(friendMemberDelete.getBody().getUsername()).isEqualTo(testUserName1);
    }

    @Test
    @Order(3)
    @DisplayName("친구 상세 조회 실패 -> (outOfBound)")
    void myFriendDetailSearchFailed() {
        // given
        myFriendAddInit();

        // when -> index 의 범위는 0 ~ 4 이다.

        // then -> 범위를 벗어날 경우 예외 발생
        assertThatThrownBy(() ->  memberFriendService.friendMemberDetailSearch(memberRequestDto.getEmail(), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(4)
    @DisplayName("친구 삭제 실패 -> (outOfBound)")
    void myFriendDeleteFailed() {
        // given
        myFriendAddInit();

        // when

        // then -> index 범위가 0 ~ 4 인데 그것을 넘어선 예외.
        assertThatThrownBy(() -> memberFriendService.friendMemberDelete(memberRequestDto.getEmail(), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void myFriendAddInit() {
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);
    }
}