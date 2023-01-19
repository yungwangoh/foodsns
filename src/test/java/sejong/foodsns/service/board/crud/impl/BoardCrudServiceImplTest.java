package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

//@DataJpaTest
@SpringBootTest
public class BoardCrudServiceImplTest {

    @Autowired
    private BoardCrudService boardCrudService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private BoardResponseDto boardResponseDto;

    @BeforeEach
    void initMember() {
        Member member = new Member("하윤", "gkdbssla97@naver.com", "4321", NORMAL);
        memberRepository.save(member);
    }

    @Nested
    @DisplayName("서비스 성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ServiceSuccess {
        @Test
        @DisplayName("게시물 등록")
        @Order(0)
        void boardCreate() {

            //given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            Board board = new Board("레시피1", "콩나물무침", findMember.getMemberRank(), 13L, 13, null,
                    findMember);
            boardResponseDto = BoardResponseDto.builder()
                    .board(board)
                    .build();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);

            //when
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto);

            //then
//            assertThat(boardCreate.getStatusCode()).isEqualTo(CREATED);
//            assertThat(getBody(boardCreate).getTitle()).isEqualTo(boardResponseDto.getTitle());
//            assertThat(getBody(boardCreate).getContent()).isEqualTo(boardResponseDto.getContent());
//            assertThat(getBody(boardCreate).getMemberResponseDto().getUsername()).isEqualTo(boardRequestDto.getMemberRequestDto().getUsername()); // Response / Request
//            assertThat(getBody(boardCreate).getMemberRank()).isEqualTo(boardResponseDto.getMemberRank());
        }

        @Test
        @DisplayName("게시물 찾기")
        @Order(1)
        void findBoard() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            boardCrudService.boardCreate(boardRequestDto);

            // when
            ResponseEntity<Optional<BoardResponseDto>> findBoard = boardCrudService.findBoard(boardRequestDto.getTitle());

            // then
            assertThat(findBoard.getStatusCode()).isEqualTo(OK);
            assertTrue(getFindBoardBody(findBoard).isPresent());
        }

        @Test
        @DisplayName("게시판 목록")
        @Order(2)
        void boardList() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            List<ResponseEntity<Optional<BoardResponseDto>>> list = new ArrayList<>();
            list.add(boardCrudService.boardCreate(getBoardRequestDto(1, findMember)));
            list.add(boardCrudService.boardCreate(getBoardRequestDto(2, findMember)));

            // when
            ResponseEntity<Optional<List<BoardResponseDto>>> boardList = boardCrudService.boardList();

            // then
            assertThat(boardList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getBoardResponseDtos(boardList).size());
        }

        private List<BoardResponseDto> getBoardResponseDtos(ResponseEntity<Optional<List<BoardResponseDto>>> boardList) {
            return boardList.getBody().get();
        }

        @Test
        @DisplayName("게시판 제목 수정")
        @Order(3)
        void boardTitleUpdate() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            String updateTitle = "검은콩나물무침";
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            boardCrudService.boardCreate(boardRequestDto);

            ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate =
                    boardCrudService.boardTitleUpdate(boardRequestDto.getTitle(), updateTitle);

            Board board = boardRepository.findBoardByTitle(getBody(boardTitleUpdate).getTitle()).get();
            // then
            assertThat(boardTitleUpdate.getStatusCode()).isEqualTo(OK);
            assertThat(board.getTitle()).isEqualTo(updateTitle);
        }

        @Test
        @DisplayName("게시물 삭제")
        @Order(4)
        void boardDelete() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            boardCrudService.boardCreate(boardRequestDto);

            // when
            boardCrudService.boardDelete(boardRequestDto);

            // then
            // 찾으려는 게시물이 없어야한다.
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<BoardResponseDto>> board =
                        boardCrudService.findBoard(boardRequestDto.getTitle());

                assertThat(board.getStatusCode()).isEqualTo(NO_CONTENT);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @AfterEach
        void deleteInit() {
            boardRepository.deleteAll();
            memberRepository.deleteAll();
        }

    }

    @Nested
    @DisplayName("서비스 실패")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class serviceFail {

        @Test
        @DisplayName("게시물 제목 중복 -> 게시물 등록 실패")
        @Order(0)
        void boardDuplicatedValidationFail() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            boardCrudService.boardCreate(boardRequestDto);

            // when
            Boolean boardTitleExistValidation = boardCrudService.boardTitleExistValidation(boardRequestDto);

            // then
            // true -> 중복
            assertTrue(boardTitleExistValidation);
        }

        @Test
        @DisplayName("찾으려는 게시물이 존재하지 않을때 예외")
        @Order(1)
        void boardFindException() {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);

            // when
            boardCrudService.boardCreate(boardRequestDto);

            // then
            assertThatThrownBy(() -> boardCrudService.findBoard(getBoardRequestDto(2, findMember).getTitle()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @AfterEach
        void deleteInit() {
            memberRepository.deleteAll();
        }
    }

    private BoardRequestDto getBoardRequestDto(int idx, Member member) {
        if(idx == 1) {
            return BoardRequestDto.builder()
                    .title("레시피1")
                    .content("콩나물무침")
                    .memberRequestDto(new MemberRequestDto(member.getId(), member.getUsername(), member.getEmail(), member.getPassword()))
                    .check(13L)
                    .recommCount(13)
                    .build();
        }
        return BoardRequestDto.builder()
                .title("레시피2")
                .content("시금치무침")
                .memberRequestDto(new MemberRequestDto(member.getId(), member.getUsername(), member.getEmail(), member.getPassword()))
                .check(13L)
                .recommCount(13)
                .build();
    }

    private BoardResponseDto getBody(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return getFindBoardBody(boardCreate).get();
    }

    private Optional<BoardResponseDto> getFindBoardBody(ResponseEntity<Optional<BoardResponseDto>> findBoard) {
        return findBoard.getBody();
    }
}
