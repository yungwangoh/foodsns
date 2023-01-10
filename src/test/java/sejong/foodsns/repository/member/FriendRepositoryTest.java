package sejong.foodsns.repository.member;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sejong.foodsns.domain.member.MemberType.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {

    }

    @AfterEach
    void initDB() {
    }

    @Test
    @Order(0)
    @DisplayName("친구 등록")
    void friendCreate() {
        // given
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        Member member1 = new Member("하윤", "qkfks1234@naver.com", "qkfks1234@A", NORMAL);
        Member member2 = new Member("임우택", "ssafy1234@ssafy.com", "rhkddh77@A", NORMAL);

        memberRepository.save(member1);
        memberRepository.save(member2);

        Friend friend = new Friend(member1);
        Friend friend1 = new Friend(member2);

        Friend friendSave = friendRepository.save(friend);
        Friend friendSave2 = friendRepository.save(friend1);

        member.setFriends(friendSave);
        member.setFriends(friendSave2);

        // when
        Member save = memberRepository.save(member);

        // then -> 친구 2명 추가
        assertThat(save.getFriends().size()).isEqualTo(2);
    }

    @Test
    @Order(1)
    @DisplayName("친구 찾기")
    void myFriendList() {
        // given
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        Member member1 = new Member("하윤", "qkfks1234@naver.com", "qkfks1234@A", NORMAL);
        Member member2 = new Member("임우택", "ssafy1234@ssafy.com", "rhkddh77@A", NORMAL);

        memberRepository.save(member1);
        memberRepository.save(member2);

        Friend friend = new Friend(member1);
        Friend friend1 = new Friend(member2);

        Friend friendSave = friendRepository.save(friend);
        Friend friendSave2 = friendRepository.save(friend1);

        member.setFriends(friendSave);
        member.setFriends(friendSave2);
        Member save = memberRepository.save(member);

        // when
        Optional<Friend> byMember_email = friendRepository.findByMember_Email("qkfks1234@naver.com");

        // then
        assertThat(byMember_email.get().getMember().getEmail()).isEqualTo("qkfks1234@naver.com");
    }
}