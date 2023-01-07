package sejong.foodsns.repository.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.FoodTag;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    List<String> title;
    List<String> content;
    List<String> userName;
    List<String> email;
    List<String> password;
    MemberType memberType;
    MemberRank memberRank;

    @BeforeEach
    void init() {
        this.title = List.of("test1", "test2", "test3");
        this.content = List.of("된장찌개", "김치찌개", "오이초무침");
        this.userName = List.of("윤광오", "하윤", "Naver");
        this.email = List.of("swager253@naver.com", "gkdbssla97@naver.com", "naver@naver.com");
        this.password = List.of("1234", "4321", "1111");
        this.memberType = MemberType.NORMAL;
        this.memberRank = MemberRank.NORMAL;

    }

    @Nested
    @DisplayName("Create")
    class createBoard {
        @DisplayName("게시물 등록 테스트")
        @Test
        void registerBoard() {

            FoodTag foodTag = new FoodTag("gfd", "fds");
            Board board = new sejong.foodsns.domain.board.Board(title.get(0), content.get(0), memberRank, 13L, 13, null,
                    new Member(userName.get(0), email.get(0), password.get(0), memberType), null, null);

            Board saveData = boardRepository.save(board);

            assertThat(saveData).isEqualTo(board);
        }
    }

    @DisplayName("게시물 추천 시 랭크 등업")
    @Test
    void advancedRankByRecommending() {
        Member member = new Member(userName.get(1), email.get(1), password.get(1), memberType);
        Board board = new Board(title.get(0), content.get(0), member.getMemberRank(),
                13L, 0, null, member, null, null);

        //멤버 추천수 총 12개 일때, BRONZE.
        for (int i = 1; i <= 12; i++)
            board.plusRecommendCount();
        member.memberRecommendUp(board.getRecommCount());
        assertThat(member.getMemberRank()).isEqualTo(MemberRank.BRONZE);

        //멤버 추천수 총 31개 일때, SILVER.
        for (int i = 1; i <= 19; i++)
            board.plusRecommendCount();
        member.memberRecommendUp(board.getRecommCount());
        assertThat(member.getMemberRank()).isEqualTo(MemberRank.SILVER);
    }

    @Nested
    @DisplayName("Read")
    class readBoard {
        @Test
        @DisplayName("등록된 게시물 \"제목\"으로 검색")
        void findRegisterBoardByTitle() {
            // given
            Long id = 1L;
            Member member = new Member(userName.get(1), email.get(1), password.get(1), memberType);
            Board board = new Board(title.get(0), content.get(0), member.getMemberRank(), 13L, 0, null, member
                    , null, null);

            memberRepository.save(member);
            Board boardSave = boardRepository.save(board);

            // when
            Board findBoard = getBoardByTitle(boardSave);

            // then
            assertThat(findBoard.getTitle()).isEqualTo(board.getTitle());
        }

        @Test
        @DisplayName("등록된 게시물 \"랭크\"별 검색")
        void findRegisterBoardsByMemberRank() {
            // given
            Long id = 1L;
            initBoardRegister();

            // when
            List<Board> boardByMemberRank1 = getBoardByMemberRank(MemberRank.NORMAL);
            List<Board> boardByMemberRank2 = getBoardByMemberRank(MemberRank.DIAMOND);

            // then
            assertThat(boardByMemberRank1.size()).isEqualTo(1);
            assertThat(boardByMemberRank2.size()).isEqualTo(2);
        }

        /**
         * "등록된 게시물 \"랭크\"별 검색" 내부 메서드 분리
         */
        private void initBoardRegister() {

            Member member1 = new Member(userName.get(0), email.get(0), password.get(0), memberType);
            member1.memberRecommendUp(5); // 멤버 랭크 -> Normal
            Member member2 = new Member(userName.get(1), email.get(1), password.get(1), memberType);
            member2.memberRecommendUp(130); // 멤버 랭크 -> Diamond

            Board board1 = new Board(title.get(0), content.get(0), member1.getMemberRank(), 13L, 0, null, member1
                    , null, null);
            Board board2 = new Board("Test3", "잔치국수", member2.getMemberRank(), 13L, 0, null, member2
                    , null, null);
            Board board3 = new Board("Test4", "김치전", member2.getMemberRank(), 13L, 0, null, member2
                    , null, null);

            memberRepository.save(member1);
            memberRepository.save(member2);

            boardRepository.save(board1);
            boardRepository.save(board2);
            boardRepository.save(board3);
        }

        @Test
        @DisplayName("\"모든\" 게시물 찾기")
        void boardFindAll() {
            // given
            Long id = 1L;
            List<Board> addBoard = getBoards();
            boardRepository.saveAll(addBoard);

            // when
            List<Board> boards = boardRepository.findAll();

            // then
            assertThat(addBoard.size()).isEqualTo(boards.size());
        }
    }

    @Nested
    @DisplayName("Update")
    class updateBoard {
        @Test
        @DisplayName("게시물 제목 수정")
        void boardUpdateName() {
            // given
            Long id = 1L;
            Member member = new Member(userName.get(0), email.get(0), password.get(0), memberType);
            memberRepository.save(member);

            String updateBoardTitle = "김치찌개 레시피(업데이트)";
            Board board = new Board(title.get(1), content.get(1), MemberRank.BRONZE, 13L, 13, null,
                    member, null,
                    null);
            Board save = boardRepository.save(board);

            // when
            save.boardTitleUpdate(updateBoardTitle);
            Board updateBoard = boardRepository.save(save);

            // then
            assertThat(updateBoard.getTitle()).isEqualTo(updateBoardTitle);
            assertThat(updateBoard.getTitle()).isNotEqualTo(title.get(1));
        }
    }

    @Nested
    @DisplayName("Delete")
    class deleteBoard {
        @Test
        @DisplayName("게시물 삭제")
        void boardDelete() {
            // given
            Long id = 1L;
            Member member = new Member(userName.get(0), email.get(0), password.get(0), memberType);
            memberRepository.save(member);

            Board board = new Board(title.get(1), content.get(1), MemberRank.BRONZE, 13L, 13, null,
                    member, null,
                    null);
            Board save = boardRepository.save(board);

            // when
            boardRepository.delete(save);
            Optional<Board> deleteBoard = boardRepository.findById(save.getId());

            // then
            assertFalse(deleteBoard.isPresent());
        }
    }

    @Nested
    @DisplayName("게시물 유효성 검증")
    class validateBoard {
        @Test
        @DisplayName("중복된 게시물 제목 예외처리")
        void validateDuplicatedBoardTitleException() throws IllegalStateException {
            // given
            Long id = 1L;
            Member member = new Member(userName.get(0), email.get(0), password.get(0), memberType);
            memberRepository.save(member);

            Board orderBoard = new Board(title.get(0), content.get(0), memberRank.NORMAL, 13L, 13, null, member,
                    null, null);
            boardRepository.save(orderBoard);

            Board saveNewBoard = new Board(title.get(0), content.get(0), memberRank.NORMAL, 13L, 13, null, member,
                    null, null);

            // when
            Board boardByTitle = getBoardByTitle(orderBoard);

            // then
            assertThatThrownBy(() -> boardEqualCheck(boardByTitle, saveNewBoard)).isInstanceOf(IllegalStateException.class);
        }
    }

    /**
     * 중복된 게시물 제목 예외 테스트 메서드
     * @param orderBoard newBoard
     */
    private void boardEqualCheck(Board orderBoard, Board newBoard) {
        if (orderBoard.getTitle().equals(newBoard.getTitle())) throw new IllegalStateException("중복된 제목입니다.");
    }

    /**
     * 제목으로 찾은 게시물을 리턴하는 테스트 메서드
     *
     * @return
     */
    private Board getBoardByTitle(Board saveBoard) {
        Optional<Board> boardId = boardRepository.findBoardByTitle(saveBoard.getTitle());
        Board board = boardId.get();
        return board;
    }

    /**
     * 랭크로 찾은 게시물을 리턴하는 테스트 메서드
     *
     * @return
     */
    private List<Board> getBoardByMemberRank(MemberRank memberRank) {
        List<Board> boards = boardRepository.findBoardByMemberRank(memberRank);
        return boards;
    }

    /**
     * 게시물을 담는 초기화 테스트 메서드
     *
     * @return
     */
    private List<Board> getBoards() {
        init();
        List<Board> addBoard = new ArrayList<>();
        List<Member> addMember = new ArrayList<>();

        Member member1 = new Member(userName.get(0), email.get(0), password.get(0), memberType);
        Member member2 = new Member(userName.get(1), email.get(1), password.get(1), memberType);

        Board board1 = new Board(title.get(0), content.get(0), memberRank.NORMAL, 13L, 13, null, member1,
                null, null);
        Board board2 = new Board(title.get(1), content.get(1), memberRank.PLATINUM, 13L, 13, null, member2,
                null, null);

        addMember.add(member1);
        addMember.add(member2);

        memberRepository.saveAll(addMember);

        addBoard.add(board1);
        addBoard.add(board2);

        return addBoard;
    }
}