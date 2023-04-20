package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.exception.http.board.NoSearchBoardException;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.board.RecommendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@SpringBootTest
public class BoardCrudServiceImplTest {

    @Autowired private BoardCrudService boardCrudService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RecommendRepository recommendRepository;

    private BoardResponseDto boardResponseDto;

    @BeforeEach
    void initMember() {
        Member member = new Member("하윤", "swager253@naver.com", "qwer1234@A", NORMAL);
        Member member1 = new Member("윤광오", "qwer1234@naver.com", "qwer1234@A", NORMAL);
        memberRepository.save(member);
        memberRepository.save(member1);
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
            List<MultipartFile> mockMultipartFiles = getMultipartFiles();

            Member findMember = memberRepository.findMemberByUsername("하윤").get();

            Board board = new Board("레시피1", "콩나물무침",13L, 13, null, findMember);

            boardResponseDto = BoardResponseDto.builder()
                    .board(board)
                    .build();

            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);

            //when
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, mockMultipartFiles);

            //then
            assertThat(boardCreate.getStatusCode()).isEqualTo(CREATED);
            assertThat(getBody(boardCreate).getTitle()).isEqualTo(boardResponseDto.getTitle());
            assertThat(getBody(boardCreate).getContent()).isEqualTo(boardResponseDto.getContent());
            assertThat(getBody(boardCreate).getMemberResponseDto().getUsername()).isEqualTo(boardRequestDto.getUsername()); // Response / Request
            assertThat(getBody(boardCreate).getMemberResponseDto().getMemberRank()).isEqualTo(boardResponseDto.getMemberResponseDto().getMemberRank());
        }

        @Test
        @DisplayName("게시물 찾기")
        @Order(1)
        void findBoard() throws IOException {
            // given
            List<MultipartFile> multipartFiles = getMultipartFiles();

            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

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
            List<MultipartFile> multipartFiles = getMultipartFiles();

            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            List<ResponseEntity<Optional<BoardResponseDto>>> list = new ArrayList<>();
            list.add(boardCrudService.boardCreate(getBoardRequestDto(1, findMember), multipartFiles));
            list.add(boardCrudService.boardCreate(getBoardRequestDto(2, findMember), multipartFiles));

            // when
            ResponseEntity<Optional<List<BoardResponseDto>>> boardList = boardCrudService.boardList();

            // then
            assertThat(boardList.getStatusCode()).isEqualTo(OK);
            assertThat(list.size()).isEqualTo(getBoardResponseDtos(boardList).size());
        }

        @Test
        @DisplayName("게시판 제목 수정")
        @Order(3)
        void boardTitleUpdate() throws IOException{
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            String updateTitle = "검은콩나물무침";
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
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
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
            boardCrudService.boardDelete(getBody(boardCreate).getId(), findMember.getUsername());

            // then
            // 찾으려는 게시물이 없어야한다.
            assertThatThrownBy(() -> {
                ResponseEntity<Optional<BoardResponseDto>> board =
                        boardCrudService.findBoardById(getBody(boardCreate).getId());

                assertThat(board.getStatusCode()).isEqualTo(NO_CONTENT);
            }).isInstanceOf(NoSearchBoardException.class);
        }

        @Test
        @DisplayName("게시물 등록할 떄 첨부파일 없이 등록")
        void whenBoardIsCreatedNotFile() throws IOException {
            // given
            List<MultipartFile> multipartFiles = new ArrayList<>();
            Member findMember = memberRepository.findMemberByUsername("하윤").get();

            Board board = new Board("레시피1", "콩나물무침",13L, 13, null,
                    findMember);

            boardResponseDto = BoardResponseDto.builder()
                    .board(board)
                    .build();

            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);

            //when
            ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // then
            assertThat(boardCreate.getStatusCode()).isEqualTo(CREATED);
            assertThat(getBody(boardCreate).getTitle()).isEqualTo(boardResponseDto.getTitle());
            assertThat(getBody(boardCreate).getContent()).isEqualTo(boardResponseDto.getContent());
            assertThat(getBody(boardCreate).getMemberResponseDto().getUsername()).isEqualTo(boardRequestDto.getUsername()); // Response / Request
            assertThat(getBody(boardCreate).getMemberResponseDto().getMemberRank()).isEqualTo(boardResponseDto.getMemberResponseDto().getMemberRank());
        }

        @Test
        @DisplayName("검색 옵션을 통하여 게시물 조회 테스트")
        void searchOptionBoardSearchTest() throws IOException {
            // given
            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // 이 문자열에 연관된 게시물을 통쨰로 조회한다.
            String content = "레시피";

            // when
            // 제목이던 본문이던 상관없다. 검색 옵션 ALL로 조회
            ResponseEntity<List<BoardResponseDto>> boards = boardCrudService.search(SearchOption.ALL, content);

            // then
            assertThat(boards.getBody().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("추천 수 증가 테스트")
        void recommendUpTest() throws IOException {
            // given
            String username = "하윤";
            Member findMember = memberRepository.findMemberByUsername(username).get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate =
                    boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
            boardCrudService.boardRecommendUp(username, getBody(boardCreate).getId());

            ResponseEntity<Optional<BoardResponseDto>> board =
                    boardCrudService.findBoardById(getBody(boardCreate).getId());

            // then
            // 추천 수 0에서 1로 증가 기댓값 1.
            assertThat(getBody(board).getRecommCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("추천 수 감소 테스트")
        void recommendDownTest() throws IOException {
            // given
            String username = "하윤";
            Member findMember = memberRepository.findMemberByUsername(username).get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate =
                    boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
            // 1이 올라가고 다시 1 내려감 디폴트 값은 0이며 1 증가 1 감소로 기댓값은 0이다.
            boardCrudService.boardRecommendUp(username, getBody(boardCreate).getId());
            boardCrudService.boardRecommendDown(username, getBody(boardCreate).getId());

            ResponseEntity<Optional<BoardResponseDto>> board =
                    boardCrudService.findBoardById(getBody(boardCreate).getId());

            // then
            assertThat(getBody(board).getRecommCount()).isEqualTo(0);
        }

        @AfterEach
        void deleteInit() {
            recommendRepository.deleteAll();
            boardRepository.deleteAll();
            memberRepository.deleteAll();
        }

        private List<BoardResponseDto> getBoardResponseDtos(ResponseEntity<Optional<List<BoardResponseDto>>> boardList) {
            return boardList.getBody().get();
        }
    }

    @Nested
    @DisplayName("서비스 실패")
    @TestClassOrder(ClassOrderer.OrderAnnotation.class)
    class serviceFail {

        @Test
        @DisplayName("게시물 제목 중복 -> 게시물 등록 실패")
        @Order(0)
        void boardDuplicatedValidationFail() throws IOException {
            // given

            Member findMember = memberRepository.findMemberByUsername("하윤").get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            boardCrudService.boardCreate(boardRequestDto, multipartFiles);

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
            List<MultipartFile> multipartFiles = getMultipartFiles();

            // when
            boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // then
            assertThatThrownBy(() -> boardCrudService.findBoardById(0L))
                    .isInstanceOf(NoSearchBoardException.class);
        }

        @Test
        @DisplayName("추천 중복 검증 같은 회원이 한 게시물에 중복으로 추천하지 못한다.")
        @Order(2)
        void recommendDuplicatedCheck() throws IOException {
            // given
            String username = "하윤";
            Member findMember = memberRepository.findMemberByUsername(username).get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate =
                    boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
            boardCrudService.boardRecommendUp(username, getBody(boardCreate).getId());

            assertThatThrownBy(() -> boardCrudService.boardRecommendUp(username, getBody(boardCreate).getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("추천 다른 회원이 추천 증가 or 감소를 하지 못한다. 오직 같은 회원만 증가 or 감소 가능")
        @Order(3)
        void recommendDownOtherMemberNotDown() throws IOException {
            // given
            String username = "하윤";
            String username1 = "윤광오";
            Member findMember = memberRepository.findMemberByUsername(username).get();
            BoardRequestDto boardRequestDto = getBoardRequestDto(1, findMember);
            List<MultipartFile> multipartFiles = getMultipartFiles();

            ResponseEntity<Optional<BoardResponseDto>> boardCreate =
                    boardCrudService.boardCreate(boardRequestDto, multipartFiles);

            // when
            boardCrudService.boardRecommendUp(username, getBody(boardCreate).getId());

            // then
            assertThatThrownBy(() -> boardCrudService.boardRecommendDown(username1, getBody(boardCreate).getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @AfterEach
        void deleteInit() {
            recommendRepository.deleteAll();
            boardRepository.deleteAll();
            memberRepository.deleteAll();
        }
    }

    private BoardRequestDto getBoardRequestDto(int idx, Member member) {

        if(idx == 1) {
            return BoardRequestDto.builder()
                    .title("레시피1")
                    .content("콩나물무침")
                    .username(member.getUsername())
                    .build();
        }
        return BoardRequestDto.builder()
                .title("레시피2")
                .content("시금치무침")
                .username(member.getUsername())
                .build();
    }

    private BoardResponseDto getBody(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return getFindBoardBody(boardCreate).get();
    }

    private Optional<BoardResponseDto> getFindBoardBody(ResponseEntity<Optional<BoardResponseDto>> findBoard) {
        return findBoard.getBody();
    }

    private List<MultipartFile> getMultipartFiles() throws IOException {
        String name = "image";
        String originalFileName = "test.jpeg";
        String contentType = "image/jpeg";
        String fileUrl = "/Users/yungwang-o/Documents/test.jpeg";

        List<MultipartFile> mockMultipartFiles = new ArrayList<>();
        mockMultipartFiles.add(new MockMultipartFile(name, originalFileName, contentType, new FileInputStream(fileUrl)));
        return mockMultipartFiles;
    }
}
