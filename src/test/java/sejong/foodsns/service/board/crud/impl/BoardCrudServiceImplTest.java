package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
public class BoardCrudServiceImplTest {

    @Autowired private BoardCrudService boardCrudService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;

    private BoardResponseDto boardResponseDto;

    @BeforeEach
    void initMember() {
        Member member = new Member("하윤", "swager253@naver.com", "rhkddh77@A", NORMAL);
        Member saveMember = memberRepository.save(member);
    }

    @Nested
    @DisplayName("서비스 성공")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ServiceSuccess {
        @Test
        @DisplayName("게시물 등록")
        @Order(0)
        void boardCreate() throws IOException {

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
            assertThat(boardCreate.getStatusCode()).isEqualTo(CREATED);
            assertThat(getBody(boardCreate).getTitle()).isEqualTo(boardResponseDto.getTitle());
            assertThat(getBody(boardCreate).getContent()).isEqualTo(boardResponseDto.getContent());
            assertThat(getBody(boardCreate).getMemberResponseDto().getUsername()).isEqualTo(boardRequestDto.getMemberRequestDto().getUsername()); // Response / Request
            assertThat(getBody(boardCreate).getMemberRank()).isEqualTo(boardResponseDto.getMemberRank());
        }

        @Test
        @DisplayName("게시물 찾기")
        @Order(1)
        void findBoard() throws IOException {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto);

            // when
            ResponseEntity<Optional<BoardResponseDto>> findBoard = boardCrudService.findBoardById(getBody(boardCreate).getId());

            // then
            assertThat(findBoard.getStatusCode()).isEqualTo(OK);
            assertTrue(getFindBoardBody(findBoard).isPresent());
        }

        @Test
        @DisplayName("게시판 목록")
        @Order(2)
        void boardList() throws IOException{
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
        void boardTitleUpdate() throws IOException{
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            String updateTitle = "검은콩나물무침";
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto);

            ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate =
                    boardCrudService.boardTitleUpdate(getBody(boardCreate).getId(), findMember.getUsername(), updateTitle);

            Board board = boardRepository.findBoardByTitle(getBody(boardTitleUpdate).getTitle()).get();
            // then
            assertThat(boardTitleUpdate.getStatusCode()).isEqualTo(OK);
            assertThat(board.getTitle()).isEqualTo(updateTitle);
        }

        @Test
        @DisplayName("게시물 삭제")
        @Order(4)
        void boardDelete() throws IOException {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto);

            // when
            boardCrudService.boardDelete(getBody(boardCreate).getId(), findMember.getUsername());

            // then
            // 찾으려는 게시물이 없어야한다.
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<BoardResponseDto>> board =
                        boardCrudService.findBoardById(getBody(boardCreate).getId());

                assertThat(board.getStatusCode()).isEqualTo(NO_CONTENT);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("검색 옵션을 통하여 게시물 조회 테스트")
        void searchOptionBoardSearchTest() throws IOException {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            boardCrudService.boardCreate(boardRequestDto);

            // 이 문자열에 연관된 게시물을 통쨰로 조회한다.
            String content = "레시피";

            // when
            // 제목이던 본문이던 상관없다. 검색 옵션 ALL로 조회
            ResponseEntity<List<BoardResponseDto>> boards = boardCrudService.search(SearchOption.ALL, content);

            // then
            assertThat(boards.getBody().size()).isEqualTo(1);
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
        void boardDuplicatedValidationFail() throws IOException {
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
        void boardFindException() throws IOException {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);

            // when
            boardCrudService.boardCreate(boardRequestDto);

            // then
            assertThatThrownBy(() -> boardCrudService.findBoardById(0L))
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
                    .memberRequestDto(new MemberRequestDto(member.getUsername(), member.getEmail(), member.getPassword()))
                    .build();
        }
        return BoardRequestDto.builder()
                .title("레시피2")
                .content("시금치무침")
                .memberRequestDto(new MemberRequestDto(member.getUsername(), member.getEmail(), member.getPassword()))
                .build();
    }

    private BoardResponseDto getBody(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return getFindBoardBody(boardCreate).get();
    }

    private Optional<BoardResponseDto> getFindBoardBody(ResponseEntity<Optional<BoardResponseDto>> findBoard) {
        return findBoard.getBody();
    }
}
