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

    @Test
    @Order(0)
    @DisplayName("친구 추가")
    void myFriendAdd() {
        // given
        Member friend = new Member("하윤", "qkfks1234@zzz.com", "qwer1234@A", NORMAL);
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        memberRepository.save(member);

        Friend f = new Friend(friend);
        f.setMember(member);

        // when
        Friend save = friendRepository.save(f);

        // then
        assertThat(save).isEqualTo(f);
        assertThat(save.getMember()).isEqualTo(member);
    }

    @Test
    @Order(1)
    @DisplayName("친구 찾기")
    void myFriendSearch() {
        // given
        Member friend = new Member("하윤", "qkfks1234@zzz.com", "qwer1234@A", NORMAL);
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        memberRepository.save(member);

        Friend f = new Friend(friend);
        f.setMember(member);

        Friend save = friendRepository.save(f);

        // when
        List<Friend> friends = friendRepository.findByMemberId(save.getMember().getId());

        // then
        assertThat(friends.get(0)).isEqualTo(f);
    }

    @Test
    @Order(2)
    @DisplayName("친구 삭제")
    void myFriendDelete() {
        // given
        Member friend = new Member("하윤", "qkfks1234@zzz.com", "qwer1234@A", NORMAL);
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        Member memberSave = memberRepository.save(member);

        Friend f = new Friend(friend);
        f.setMember(member);
        friendRepository.save(f);

        List<Friend> friends = friendRepository.findByMemberId(memberSave.getId());
        Friend findFriend = friends.get(0);

        // when
        friendRepository.delete(findFriend);
        List<Friend> friendList = friendRepository.findByMemberId(memberSave.getId());

        // then
        assertThat(friendList.size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    @DisplayName("친구 목록 조회")
    void myFriendList() {
        // given
        Member friend = new Member("하윤", "qkfks1234@zzz.com", "qwer1234@A", NORMAL);
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        Member memberSave = memberRepository.save(member);

        Friend f = new Friend(friend);
        f.setMember(member);
        friendRepository.save(f);

        // when
        List<Friend> friends = friendRepository.findByMemberId(memberSave.getId());

        // then
        assertThat(friends.size()).isEqualTo(1);
    }

    @AfterEach
    void initDB() {
        friendRepository.deleteAll();
        memberRepository.deleteAll();
    }
}