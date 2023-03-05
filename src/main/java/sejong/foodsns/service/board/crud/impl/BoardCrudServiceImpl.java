package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.exception.http.DuplicatedException;
import sejong.foodsns.exception.http.board.NoSearchBoardException;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.querydsl.board.BoardQueryDslRepository;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.BoardFileCrudService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardCrudServiceImpl implements BoardCrudService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardQueryDslRepository boardQueryDslRepository;
    private final BoardFileCrudService boardFileCrudService;

    /**
     * 게시물 생성 -> 성공 201, 실패 404
     * @param boardRequestDto
     * @return 게시물 DTO
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<BoardResponseDto>> boardCreate(BoardRequestDto boardRequestDto, List<MultipartFile> multipartFiles) throws IOException {

        duplicatedCheckBoardTitle(boardRequestDto);

        List<BoardFile> boardFiles = boardFileCrudService.saveBoardFiles(multipartFiles);
        Member findMember = memberRepository.findMemberByUsername(boardRequestDto.getMemberRequestDto().getUsername()).get();
        Board board = boardClassCreated(boardRequestDto, findMember, boardFiles);

        Board save = boardRepository.save(board);
        return new ResponseEntity<>(of(new BoardResponseDto(save)), CREATED);
    }

    /**
     * 게시물 제목 수정 -> 성공 ?, 실패 ?
     * @param id
     * @param username
     * @param updateTitle
     * @return 게시물 DTO, HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate(Long id, String username, String updateTitle) {
        Optional<Board> board = getBoardReturnByOptionalBoardId(id);

        if(!getBoard(board).getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("수정 하려는 게시물 작성자가 본인과 다릅니다.");
        } else {
            Board updateBoard = getBoard(board).boardTitleUpdate(updateTitle);

            Board save = boardRepository.save(updateBoard);

            return new ResponseEntity<>(of(new BoardResponseDto(save)), OK);
        }
    }

    /**
     * 게시물 삭제 -> 성공 ?, 실패 ?
     * @param id
     * @param username
     * @return HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<BoardResponseDto>> boardDelete(Long id, String username) {

        Optional<Board> board = getBoardReturnByOptionalBoardId(id);

        // 게시물 작성자와 본인이 다르면 삭제 불가능.
        if(!getBoard(board).getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("삭제 하려는 게시물 작성자가 본인과 다릅니다.");
        }
        else { // 게시물 작성자와 본인이 동일하면 삭제.
            //Token으로 할 것이므로 Jpa delete 작동하는지만 임시 확인.
            boardRepository.delete(getBoard(board));

            return new ResponseEntity<>(NO_CONTENT);
        }
    }

    /**
     * id를 통한 게시물 조회
     * @param id
     * @return 게시물, OK
     */
    @Override
    public ResponseEntity<Optional<BoardResponseDto>> findBoardById(Long id) {
        Optional<Board> board = getBoardOptionalById(id);

        return new ResponseEntity<>(of(new BoardResponseDto(getBoard(board))), OK);
    }

    /**
     * 검색 옵션을 통해 게시물 조회
     * @param searchOption 제목, 본문, 제목 + 본문 검색 옵션
     * @param content 조회할 제목, 본문 제목 + 본문 문자열
     * @return 게시물 리스트, OK
     */
    @Override
    public ResponseEntity<List<BoardResponseDto>> search(SearchOption searchOption, String content) {

        List<Board> boards = boardQueryDslRepository.search(searchOption, content);
        List<BoardResponseDto> collect = boards.stream().map(BoardResponseDto::new).collect(toList());

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 게시판 목록 -> 성공 ?
     * @return 게시물 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<BoardResponseDto>>> boardList() {
        List<Board> boards = boardRepository.findAll();

        Optional<List<BoardResponseDto>> collect = of(boards.stream()
                .map(BoardResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 게시물 제목 중복 검사
     * @param boardRequestDto
     * @return 중복 true, 아니면 false
     */
    @Override
    public Boolean boardTitleExistValidation(BoardRequestDto boardRequestDto) {
        return boardRepository.existsBoardByTitle(boardRequestDto.getTitle());
    }

    /**
     * Optional Board -> return board
     * @param board
     * @return 게시물
     */
    private Board getBoard(Optional<Board> board) {
        return board.get();
    }

    /**
     * 게시물 제목 중복 검증
     * @param boardRequestDto
     */
    private void duplicatedCheckBoardTitle(BoardRequestDto boardRequestDto) {
        Boolean isDuplicated = boardRepository.existsBoardByTitle(boardRequestDto.getTitle());
        if(isDuplicated)
            throw new DuplicatedException("중복된 제목입니다.");
    }

    /**
     * 게시물 객체 생성
     * @param boardRequestDto
     * @return 게시물
     */
    private Board boardClassCreated(BoardRequestDto boardRequestDto, Member member,
                                    List<BoardFile> boardFiles) {
        return Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .memberRank(boardRequestDto.toEntity().getMemberRank())
                .check(0L)
                .recommCount(0)
                .foodTag(null)
                .member(member)
                .build();
    }

    /**
     * 게시물 반환하는 로직 Optional
     * @param title
     * @return 게시물 존재 X -> Exception
     */
    private Optional<Board> getBoardReturnByOptionalBoardId(Long id) {
        return of(boardRepository.findById(id)
                .orElseThrow(() -> new NoSearchBoardException("게시물이 존재하지 않습니다.")));
    }

    /**
     * 게시물 반환하는 로직 Optional
     * @param id 게시물 id
     * @return 게시물 존재 X -> Exception
     */
    private Optional<Board> getBoardOptionalById(Long id) {
        return of(boardRepository.findById(id))
                .orElseThrow(() -> new NoSearchBoardException("게시물이 존재하지 않습니다,"));
    }
}
