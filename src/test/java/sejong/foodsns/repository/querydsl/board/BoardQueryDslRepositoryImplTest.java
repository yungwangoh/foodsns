package sejong.foodsns.repository.querydsl.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.QBoard;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@DataJpaTest
public class BoardQueryDslRepositoryImplTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardQueryRepository boardQueryDslRepository;

    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void init() {
        jpaQueryFactory = new JPAQueryFactory(em);

        Member member = new Member("윤광오", "swager253@fdsfds.com", "qwer1234@!", NORMAL);
        Board board = new Board("안녕", "안녕하세요",
                20L, 20, null, member);
        Board board1 = new Board("안녕하세욧!", "안녕하세요",
                20L, 20, null, member);
        Board board2 = new Board("안녕하시렵니까", "안녕하세요",
                20L, 20, null, member);

        memberRepository.save(member);
        boardRepository.save(board);
        boardRepository.save(board1);
        boardRepository.save(board2);
    }

    @Test
    @DisplayName("회원이 어떤 게시물을 작성했는지 확인하는 테스트")
    void memberBoardCreateCheck() {
        // given
        String email = "swager253@fdsfds.com";
        String username = "윤광오";
        QBoard qBoard = new QBoard("b");

        // when
        // 회원 닉네임으로 조회
        List<Board> boards = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.member.username.eq(username))
                .fetch();

        // 회원 이메일로 조회
        List<Board> boards1 = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.member.email.eq(email))
                .fetch();

        // then
        assertThat(boards.size()).isEqualTo(3);
        assertThat(boards1.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시물 제목으로 조회하여 몇개의 게시물이 나오는지 확인 테스트")
    void boardTitleCountBoardCheck() {
        // given
        String title = "안녕하세요";
        QBoard qBoard = new QBoard("b");

        // when
        List<Board> boards = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.title.eq(title))
                .fetch();

        // then
        assertThat(boards.size()).isEqualTo(3);
        boards.forEach(board -> {
            assertThat(board.getTitle()).isEqualTo(title);
        });
    }

    @Test
    @DisplayName("한가지 이상의 제목만 쳐도 연관된 제목의 게시물이 나오는 테스트")
    void oneMoreTitleFetchBoard() {
        // given
        String title = "안";
        String wrongTitle = "크크";
        QBoard qBoard = new QBoard("b");

        // when
        List<Board> boards = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.title.contains(title))
                .fetch();

        List<Board> boards1 = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.title.contains(wrongTitle))
                .fetch();

        // then
        assertThat(boards.size()).isEqualTo(3);
        assertThat(boards1.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("제목 또는 본문을 조회하여 관련된 게시물 검색 그리고 한가지 이상의 문자열을 검색할 경우도 포함")
    void titleOrContentSearchBoard() {
        // given
        String title = "안녕";
        String title1 = "안녕하시렵니까";
        String content = "안녕하세";

        // when
        List<Board> boards = boardQueryDslRepository.search(SearchOption.ALL, title);
        List<Board> boards1 = boardQueryDslRepository.search(SearchOption.CONTENT, content);
        List<Board> boards2 = boardQueryDslRepository.search(SearchOption.ALL, title1);

        // then
        assertThat(boards.size()).isEqualTo(3);
        assertThat(boards1.size()).isEqualTo(3);
        assertThat(boards2.size()).isEqualTo(1);
    }
}
