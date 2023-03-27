package sejong.foodsns.service.search.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.comment.CommentResponseDto;
import sejong.foodsns.dto.reply.ReplyResponseDto;
import sejong.foodsns.dto.search.IntegratedSearchResponseDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.search.IntegratedSearchService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegratedSearchServiceImplTest {

    @Autowired
    private IntegratedSearchService integratedSearchService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @BeforeEach
    void init() {
        Member member = new Member("윤광오", "qwer1234@naver.com", "qwer1234@A", MemberType.NORMAL);
        Member memberSave = memberRepository.save(member);

        Board board = new Board("김치찌개", "맛있다", 0L, 0, null, memberSave);
        Board boardSave = boardRepository.save(board);

        Comment comment = new Comment("맛있어요!", 0, 0, boardSave, memberSave);
        Comment commentSave = commentRepository.save(comment);

        Reply reply = new Reply("좋아요", 0, 0, commentSave, memberSave);
        replyRepository.save(reply);
    }

    @Test
    @Order(0)
    @DisplayName("게시물 통합 검색 테스트")
    void boardIntegratedSearchTest() {
        // given
        String username = "윤";
        String content = "김치";

        // when
        ResponseEntity<List<BoardResponseDto>> boardIntegratedSearch =
                integratedSearchService.boardIntegratedSearch(content);

        // then
        assertThat(boardIntegratedSearch.getBody().size()).isEqualTo(1);
        boardIntegratedSearch.getBody().forEach(boardResponseDto -> {
            assertThat(boardResponseDto.getTitle()).isEqualTo("김치찌개");
            assertThat(boardResponseDto.getContent()).isEqualTo("맛있다");
            assertThat(boardResponseDto.getMemberResponseDto().getUsername()).isEqualTo("윤광오");
        });
    }

    @Test
    @Order(1)
    @DisplayName("댓글 통합 검색 테스트")
    void commentIntegratedSearchTest() {
        // given
        String username = "윤";
        String content = "맛있";

        // when
        ResponseEntity<List<CommentResponseDto>> commentIntegratedSearch =
                integratedSearchService.commentIntegratedSearch(content);

        // then
        assertThat(commentIntegratedSearch.getBody().size()).isEqualTo(1);
        commentIntegratedSearch.getBody().forEach(commentResponseDto -> {
            assertThat(commentResponseDto.getContent()).isEqualTo("맛있어요!");
            assertThat(commentResponseDto.getMemberResponseDto().getUsername())
                    .isEqualTo("윤광오");
        });
    }

    @Test
    @Order(2)
    @DisplayName("대댓글 통합 검색 테스트")
    void replyIntegratedSearchTest() {
        // given
        String username = "윤";
        String content = "좋아";

        // when
        ResponseEntity<List<ReplyResponseDto>> replyIntegratedSearch =
                integratedSearchService.replyIntegratedSearch(content);

        // then
        assertThat(replyIntegratedSearch.getBody().size()).isEqualTo(1);
        replyIntegratedSearch.getBody().forEach(replyResponseDto -> {
            assertThat(replyResponseDto.getContent()).isEqualTo("좋아요");
            assertThat(replyResponseDto.getMemberResponseDto().getUsername()).isEqualTo("윤광오");
        });
    }

    @Test
    @Order(3)
    @DisplayName("모든 통합 검색 테스트")
    void integratedSearchTest() {
        // given
        String username = "윤";
        String boardContent = "김치";
        String commentContent = "맛있";
        String replyContent = "좋아";

        // when
        ResponseEntity<IntegratedSearchResponseDto> integratedSearch =
                integratedSearchService.integratedSearch(username);

        // then
        assertThat(integratedSearch.getBody().getBoardResponseDtos().size()).isEqualTo(1);
        assertThat(integratedSearch.getBody().getCommentResponseDtos().size()).isEqualTo(1);
        assertThat(integratedSearch.getBody().getReplyResponseDtos().size()).isEqualTo(1);
    }

    @AfterEach
    void dbInit() {
        replyRepository.deleteAll();
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }
}