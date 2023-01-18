package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberFriendService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional
class MemberFriendServiceImplTest {

    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private MemberCrudService memberCrudService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;

    private static MemberRequestDto memberRequestDto;
    private static MemberRequestDto memberRequestDto1;
    private static MemberRequestDto memberRequestDto2;

    private static String testUserName;
    private static String testUserName1;

    @BeforeAll
    static void init() {
        memberRequestDto = MemberRequestDto.builder()
                .email("swager253@zzz.com")
                .password("zzz10324@A")
                .username("윤광오")
                .build();

        memberRequestDto1 = MemberRequestDto.builder()
                .email("qkfks1234@zzz.com")
                .password("zzz10324@A")
                .username("하윤")
                .build();

        memberRequestDto2 = MemberRequestDto.builder()
                .email("ssapper234@zzz.com")
                .password("zzz10324@A")
                .username("임우택")
                .build();

        testUserName = "하윤";
        testUserName1 = "임우택";
    }

    @AfterEach
    void initDB() {
        friendRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Order(0)
    @DisplayName("친구 추가 성공")
    void myFriendAddingServiceSuccess() {
        // given
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);

        // when
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName1);

        ResponseEntity<List<MemberFriendResponseDto>> memberList = memberFriendService.friendMemberList(memberRequestDto.getEmail());

        // then -> 친구 2명 저장 -> 기댓값 2
        assertThat(getBody(memberList).size()).isEqualTo(2);
        assertThat(getBody(memberList).get(0).getUsername()).isEqualTo(testUserName);
        assertThat(getBody(memberList).get(1).getUsername()).isEqualTo(testUserName1);
    }

    @Test
    @Order(1)
    @DisplayName("친구 상세 조회 성공")
    void myFriendSearchSuccess() {
        // given
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);

        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);

        // 친구 리스트를 꺼내온다.
        ResponseEntity<List<MemberFriendResponseDto>> friendMemberList = memberFriendService.friendMemberList(memberRequestDto.getEmail());

        // 친구 index 를 확인하고 친구의 Dto를 반환
        MemberFriendResponseDto memberFriendResponseDto = getBody(friendMemberList).get(0);

        // 친구의 정보를 조회
        ResponseEntity<Optional<MemberResponseDto>> friend = memberCrudService.findMember(memberFriendResponseDto.getEmail());

        // when -> 친구의 정보를 조회
        ResponseEntity<MemberResponseDto> listFriend =
                memberFriendService.friendMemberDetailSearch(memberRequestDto.getEmail(), 0);

        // then -> 기댓값 둘의 객체는 같다.
        assertThat(listFriend.getBody()).isEqualTo(friend.getBody().get());
    }

    @Test
    @Order(2)
    @DisplayName("친구 삭제 성공")
    void myFriendDeleteSuccess() {
        // given
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);

        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName);
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), testUserName1);

        // when
        ResponseEntity<MemberFriendResponseDto> friendMemberDelete =
                memberFriendService.friendMemberDelete(memberRequestDto.getEmail(), 1);

        ResponseEntity<List<MemberFriendResponseDto>> friendMemberList =
                memberFriendService.friendMemberList(memberRequestDto.getEmail());

        // then
        assertThat(getBody(friendMemberList).size()).isEqualTo(1);
        assertThat(friendMemberDelete.getBody().getUsername()).isEqualTo(testUserName1);
    }

    @Test
    @Order(3)
    @DisplayName("친구 상세 조회 실패 -> (outOfBound)")
    void myFriendDetailSearchFailed() {
        // given
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);

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
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);
        memberCrudService.memberCreate(memberRequestDto2);

        // when

        // then -> index 범위가 0 ~ 4 인데 그것을 넘어선 예외.
        assertThatThrownBy(() -> memberFriendService.friendMemberDelete(memberRequestDto.getEmail(), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(5)
    @DisplayName("회원 자신을 친구 리스트에 추가했을때에 예외")
    void mySelfFriendListAdd() {
        // given
        memberCrudService.memberCreate(memberRequestDto);

        // when

        // then -> 기댓값 : 예외
        assertThatThrownBy(() -> memberFriendService
                .friendMemberAdd(memberRequestDto.getEmail(), memberRequestDto.getEmail()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(6)
    @DisplayName("친구 리스트의 중복 허용하지 않음.")
    void myFriendListDuplicatedCheck() {
        // given
        memberCrudService.memberCreate(memberRequestDto);
        memberCrudService.memberCreate(memberRequestDto1);

        // when
        memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), memberRequestDto1.getUsername());

        // then -> 기댓값 :  예외
        assertThatThrownBy(() -> memberFriendService.friendMemberAdd(memberRequestDto.getEmail(), memberRequestDto1.getUsername()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static List<MemberFriendResponseDto> getBody(ResponseEntity<List<MemberFriendResponseDto>> memberList) {
        return memberList.getBody();
    }
}