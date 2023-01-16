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

    @BeforeEach
    void init() {
        member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        member1 = new Member("하윤", "qkfks1234@naver.com", "qkfks1234@A", NORMAL);
        member2 = new Member("임우택", "ssafy1234@ssafy.com", "rhkddh77@A", NORMAL);
    }

    @AfterEach
    void initDB() {
        friendRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Order(0)
    @DisplayName("친구 등록")
    void friendCreate() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        Friend friendSave = new Friend(save1);
        Friend friendSave2 = new Friend(save2);

        save.setFriends(friendSave);
        save.setFriends(friendSave2);

        friendSave.setMember(save);
        friendSave2.setMember(save);

        // when
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(save.getEmail());


        // then -> 친구 2명 추가
        assertThat(getMember(memberByEmail).getFriends().size()).isEqualTo(2);
        assertThat(getMember(memberByEmail).getFriends().get(0)).isEqualTo(friendSave);
        assertThat(getMember(memberByEmail).getFriends().get(1)).isEqualTo(friendSave2);
    }

    @Test
    @Order(1)
    @DisplayName("친구 찾아서 상세 조회")
    void myFriendsSearch() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);

        Friend friendSave = new Friend(save1);

        save.setFriends(friendSave);

        friendSave.setMember(save);

        // when
        Optional<Member> memberByUsername = memberRepository.findMemberByUsername(save.getUsername());

        // then -> 친구 리스트엔 친구 정보가 있음. -> 추가한 친구 정보와 리스트에 존재하는 친구 비교
        assertThat(getMember(memberByUsername).getFriends().get(0)).isEqualTo(friendSave);
    }

    @Test
    @Order(2)
    @DisplayName("친구 삭제")
    void myFriendDelete() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        Friend friendSave = new Friend(save1);
        Friend friendSave2 = new Friend(save2);

        save.setFriends(friendSave);
        save.setFriends(friendSave2);

        friendSave.setMember(save);
        friendSave2.setMember(save);

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
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        Friend friendSave = new Friend(save1);
        Friend friendSave2 = new Friend(save2);

        save.setFriends(friendSave);
        save.setFriends(friendSave2);

        friendSave.setMember(save);
        friendSave2.setMember(save);

        // when
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail("swager253@naver.com");

        // then
        assertThat(getFriends(memberByEmail).size()).isEqualTo(2);
        assertThat(getMember(memberByEmail).getFriends().get(0)).isEqualTo(friendSave);
        assertThat(getMember(memberByEmail).getFriends().get(1)).isEqualTo(friendSave2);
    }

    @Test
    @Order(4)
    @DisplayName("블랙리스트인 회원을 친구 추가할 수 없음")
    void myFriendNotAddBlackListMember() {
        // given
        Member black = new Member("한재경", "han1234@naver.com", "gfgfdgdsd@A", BLACKLIST);
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

    @Test
    @Order(5)
    @DisplayName("회원의 친구 목록 레포 테스트 join fetch 적용")
    void myFriendListRepoTestJoinFetch() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);

        Friend friendSave = new Friend(save1);

        save.setFriends(friendSave);

        friendSave.setMember(save);

        // when
        List<Friend> friendList = friendRepository.findByMemberId(save.getId());

        // then
        assertThat(friendList.size()).isEqualTo(1);
        assertThat(friendList.get(0)).isEqualTo(friendSave);
    }

    @Test
    @Order(6)
    @DisplayName("회원의 친구 목록 레포 테스트 join fetch 미적용")
    void myFriendListRepoTestNotJoinFetch() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);

        Friend friendSave = new Friend(save1);

        save.setFriends(friendSave);

        friendSave.setMember(save);

        // when
        List<Friend> friendByMemberId = friendRepository.findByMemberId(save.getId());

        // then
        assertThat(friendByMemberId.size()).isEqualTo(1);
    }

    @Test
    @Order(7)
    @DisplayName("친구를 조회하여 어느 회원과 친구를 맺었는지 조회")
    void friendSearchWhoMember() {
        // given
        Member save = memberRepository.save(member);
        Member save1 = memberRepository.save(member1);

        Friend friendSave = new Friend(save1);

        save.setFriends(friendSave);

        friendSave.setMember(save);

        // when
        Optional<Member> memberSearch =
                memberRepository.findMemberByUsername(save.getUsername());

        Optional<Member> memberByFriends =
                memberRepository.findMemberByFriends(getFriends(memberSearch).get(0).getId());

        // then
        assertThat(memberByFriends.get()).isEqualTo(save);
    }

    /**
     * 회원 타입이 블랙리스트인지 구분
     * @param memberType
     * @return 블랙이면 true, 아니면 false
     */
    private static boolean notAddBlackListMember(MemberType memberType) {
        return BLACKLIST == memberType;
    }

    /**
     * 회원의 친구 추가 초기화
     */
    private void memberFriendAddInit() {

    }

    /**
     * 회원이 가지고 있는 친구 목록
     * @param memberByEmail
     * @return 친구 목록
     */
    private static List<Friend> getFriends(Optional<Member> memberByEmail) {
        return getMember(memberByEmail).getFriends();
    }


    /**
     * 친구 회원 정보
     * @param member
     * @return 회원
     */
    private static Member getMember(Friend member) {
        return member.getMember();
    }

    private static Member getMember(Optional<Member> member) {
        return member.get();
    }
}