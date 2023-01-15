package sejong.foodsns.repository.member;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static sejong.foodsns.domain.member.MemberType.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private MemberRepository memberRepository;
    private static Member member;
    private Member member1;
    private Member member2;
    private Friend friend;
    private Friend friend1;

    @BeforeEach
    void init() {
        member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        member1 = new Member("하윤", "qkfks1234@naver.com", "qkfks1234@A", NORMAL);
        member2 = new Member("임우택", "ssafy1234@ssafy.com", "rhkddh77@A", NORMAL);

        friend = new Friend(member1);
        friend1 = new Friend(member2);
    }

    @AfterEach
    void initDB() {
    }

    @Test
    @Order(0)
    @DisplayName("친구 등록")
    void friendCreate() {
        // given
        memberFriendAddInit();

        // when
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail("swager253@naver.com");

        // then -> 친구 2명 추가
        assertThat(getFriends(of(getMember(memberByEmail))).size()).isEqualTo(2);
    }

    @Test
    @Order(1)
    @DisplayName("친구 찾기")
    void myFriendsSearch() {
        // given
        memberFriendAddInit();
        Member save = memberRepository.save(member);
        Optional<Member> id = memberRepository.findById(save.getId());

        // when
        List<Friend> friends = getMember(id).getFriends();

        // then
        assertThat(friends.size()).isEqualTo(2);
        assertThat(friends.get(0).getMember().getUsername()).isEqualTo(member1.getUsername());
        assertThat(friends.get(1).getMember().getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("친구 삭제")
    void myFriendDelete() {
        // given
        memberFriendAddInit();
        Member save = memberRepository.save(member);

        // when -> 친구 리스트에서 삭제
        getFriends(of(save)).remove(0);

        // then -> 친구 리스트에 2명이 저장 -> 1명 삭제 -> 최종 친구 수는 1명
        assertThat(getFriends(of(save)).size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @DisplayName("친구 목록")
    void myFriendList() {
        // given
        memberFriendAddInit();
        memberRepository.save(member);

        // when
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail("swager253@naver.com");

        // then
        assertThat(getFriends(memberByEmail).size()).isEqualTo(2);
        assertThat(getFriends(memberByEmail).get(0).getMember().getEmail()).isEqualTo(getMember(friend).getEmail());
        assertThat(getFriends(memberByEmail).get(1).getMember().getEmail()).isEqualTo(getMember(friend1).getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("블랙리스트인 회원을 친구 추가할 수 없음")
    void myFriendNotAddBlackListMember() {
        // given
        Member black = new Member("한재경", "han1234@naver.com", "rhkddh77@A", BLACKLIST);
        memberRepository.save(member);
        Member save = memberRepository.save(black);
        Friend blackFriendSave = friendRepository.save(new Friend(save));

        // when
        if(!notAddBlackListMember(black.getMemberType())) {
            member.setFriends(blackFriendSave);
        }

        // then -> 블랙리스트 친구는 추가 불가능 -> 기대값 0
        assertThat(member.getFriends().size()).isEqualTo(0);
    }

    /**
     * 회원 타입이 블랙리스트인지 구분
     * @param memberType
     * @return 블랙이면 true, 아니면 false
     */
    private boolean notAddBlackListMember(MemberType memberType) {
        return BLACKLIST == memberType;
    }

    /**
     * 회원의 친구 추가 초기화
     */
    private void memberFriendAddInit() {
        memberRepository.save(member);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Friend friendSave = friendRepository.save(friend);
        Friend friendSave2 = friendRepository.save(friend1);

        member.setFriends(friendSave);
        member.setFriends(friendSave2);
    }

    /**
     * 회원이 가지고 있는 친구 목록
     * @param memberByEmail
     * @return 친구 목록
     */
    private List<Friend> getFriends(Optional<Member> memberByEmail) {
        return getMember(memberByEmail).getFriends();
    }


    /**
     * 친구 회원 정보
     * @param member
     * @return 회원
     */
    private Member getMember(Friend member) {
        return member.getMember();
    }

    private static Member getMember(Optional<Member> member) {
        return member.get();
    }
}