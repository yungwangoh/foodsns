package sejong.foodsns.repository.querydsl.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.QBoard;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.QFriend;
import sejong.foodsns.domain.member.QMember;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@DataJpaTest
class MemberQueryDslRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private BoardRepository boardRepository;
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void init() {
        jpaQueryFactory = new JPAQueryFactory(em);

        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        Member friend = new Member("하윤", "qwer1234@zzz.com", "qwer1234@A", NORMAL);
        Board board = new Board("test", "안녕하세요!", member.getMemberRank(), 130L,
                10, null, member);

        memberRepository.save(member);
        Friend f = new Friend(friend);
        f.setMember(member);

        friendRepository.save(f);

        member.setBoards(board);
        boardRepository.save(board);
    }

    @Test
    @DisplayName("회원 이름으로 찾기")
    void queryDslMemberSearchTest() {
        // given
        String userName = "윤광오";
        QMember qMember = new QMember("m");

        // when
        Member member = jpaQueryFactory.selectFrom(qMember)
                .where(qMember.username.eq(userName))
                .fetchOne();

        // then
        assertThat(member.getUsername()).isEqualTo(userName);
    }

    @Test
    @DisplayName("회원 id로 친구 찾기")
    void queryDslFriendMemberSearchTest() {
        // given
        String friendName = "하윤";
        String userName = "윤광오";

        QMember qMember = new QMember("m");
        QFriend qFriend = new QFriend("f");

        // when
        List<Friend> friends = jpaQueryFactory.selectFrom(qFriend)
                .join(qFriend.member, qMember).fetchJoin()
                        .where(qMember.username.eq(userName))
                                .fetch();

        // then
        assertThat(friends.size()).isEqualTo(1);
        assertThat(friends.get(0).getFriendName()).isEqualTo(friendName);
    }

    @Test
    @DisplayName("친구를 조회하여 어떤 친구를 맺었는지 확인하는 테스트")
    void queryDslMakeFriendWithSomeOneTest() {
        // given
        String friendName = "하윤";
        String userName = "윤광오";

        QFriend qFriend = new QFriend("f");

        // when
        Friend friend = jpaQueryFactory.selectFrom(qFriend)
                .where(qFriend.friendName.eq(friendName))
                        .fetchOne();

        // then
        assertThat(friend.getMember().getUsername()).isEqualTo(userName);
    }

    @Test
    @DisplayName("회원 목록 가져오기")
    void queryDslMemberList() {
        // given
        String userName = "윤광오";
        QMember qMember = new QMember("m");

        // when
        List<Member> members = jpaQueryFactory.selectFrom(qMember)
                .fetch();

        // then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo(userName);
    }

    @Test
    @DisplayName("회원 이름으로 게시판 조회")
    void queryDslMemberNameBoardSearch() {
        // given
        String userName = "윤광오";
        String title = "test";
        QMember qMember = new QMember("m");

        // when
        Member member = jpaQueryFactory.selectFrom(qMember)
                .where(qMember.username.eq(userName))
                .fetchOne();

        // then
        assertThat(member.getBoards().get(0).getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("게시판을 조회하여 어떤 회원이 작성했는지 확인")
    void queryDslBoardSearchWriteSomeOneCheck() {
        // given
        String userName = "윤광오";
        QBoard qBoard = new QBoard("b");

        // when
        Board board = jpaQueryFactory.selectFrom(qBoard)
                .fetchOne();

        // then
        assertThat(board.getMember().getUsername()).isEqualTo(userName);
    }

    @AfterEach
    void initDB() {
        friendRepository.deleteAll();
        memberRepository.deleteAll();
    }
}