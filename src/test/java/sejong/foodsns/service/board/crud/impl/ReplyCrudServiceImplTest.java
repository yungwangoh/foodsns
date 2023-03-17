package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.CommentRequestDto;
import sejong.foodsns.dto.board.ReplyRequestDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.CommentRepository;
import sejong.foodsns.repository.board.ReplyRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.CommentCrudService;
import sejong.foodsns.service.board.crud.ReplyCrudService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static sejong.foodsns.domain.member.MemberType.*;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ReplyCrudServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardCrudService boardCrudService;
    @Autowired
    private CommentCrudService commentCrudService;
    @Autowired
    private ReplyCrudService replyCrudService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    private MemberRequestDto memberRequestDto;
    private BoardRequestDto boardRequestDto;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void init() throws IOException {

        // file
        List<MultipartFile> multipartFiles = new ArrayList<>();

        // member
        Member member = new Member("윤광오", "qkfks1234@daum.net", "qwer1234!A", NORMAL);
        Member saveMember = memberRepository.save(member);

        Member member1 = new Member("하윤", "swager253@daum.net", "qwer1234!A", NORMAL);
        Member saveMember1 = memberRepository.save(member1);
        memberRequestDto = new MemberRequestDto(saveMember1.getUsername(), saveMember1.getEmail(), saveMember1.getPassword());

        // board
        boardRequestDto = new BoardRequestDto("김치찌개 레시피", "김치찌개 굳", saveMember);
        boardCrudService.boardCreate(boardRequestDto, multipartFiles);

        // comment
        commentRequestDto = new CommentRequestDto("좋아요", memberRequestDto, boardRequestDto);
        commentCrudService.commentCreate(commentRequestDto);
    }

    @Nested
    @DisplayName("서비스 성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Success {

        @Test
        @Order(0)
        @DisplayName("대댓글 등록 성공")
        void replyCreate() {
            // given
            String content = "좋아";
            ReplyRequestDto replyRequestDto = new ReplyRequestDto(content, boardRequestDto, commentRequestDto);

            // when
            replyCrudService.replyCreate()

            // then

        }

        @AfterEach
        void dbInit() {
            replyRepository.deleteAll();
            commentRepository.deleteAll();
            boardRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }

    @Nested
    @DisplayName("서비스 실패")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Fail {


        @AfterEach
        void dbInit() {
            replyRepository.deleteAll();
            commentRepository.deleteAll();
            boardRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }
}