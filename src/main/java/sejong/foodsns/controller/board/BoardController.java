package sejong.foodsns.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.file.util.BoardFileStorage;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.board.querydsl.SearchOptionDto;
import sejong.foodsns.dto.board.update.BoardUpdateTitleDto;
import sejong.foodsns.service.board.crud.BoardCrudService;
import sejong.foodsns.service.board.crud.BoardFileCrudService;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.service.board.crud.message.BoardSuccessOrFailedMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class BoardController {

    private final BoardCrudService boardCrudService;
    private final BoardFileStorage boardFileStorage;

    /**
     * 게시물 등록
     *
     * @param boardRequestDto
     * @return 게시물, CREATE
     */
    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> boardCreate(@RequestPart(value = "board") @Valid BoardRequestDto boardRequestDto,
                                                        @RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles) throws IOException {

        ResponseEntity<Optional<BoardResponseDto>> boardCreate = boardCrudService.boardCreate(boardRequestDto, multipartFiles);

        return new ResponseEntity<>(getBoard(boardCreate), boardCreate.getStatusCode());
    }

    /**
     * 게시물 검색
     * @param id 게시물 id
     * @return 게시물 정보, 성공 - OK, - 실패 - NOT_FOUND
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> boardSearchById(@PathVariable Long id) {

        ResponseEntity<Optional<BoardResponseDto>> board = boardCrudService.findBoardById(id);

        return new ResponseEntity<>(getBoard(board), board.getStatusCode());
    }

    /**
     * 게시물 제목 수정
     *
     * @param boardUpdateTitleDto
     * @return 게시물 제목 수정 완료, OK
     */
    @PatchMapping("/board/title")
    public ResponseEntity<String> boardUpdateTitle(@RequestBody @Valid BoardUpdateTitleDto boardUpdateTitleDto) {

        ResponseEntity<Optional<BoardResponseDto>> titleUpdate =
                boardCrudService.boardTitleUpdate(boardUpdateTitleDto.getId(),
                        boardUpdateTitleDto.getMemberRequestDto().getUsername(),
                        boardUpdateTitleDto.getUpdateTitle());

        return new ResponseEntity<>(BOARD_TITLE_UPDATE_SUCCESS, titleUpdate.getStatusCode());
    }

    /**
     * 게시물 목록 조회
     *
     * @return 회원 목록, OK
     */
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> boards() {

        ResponseEntity<Optional<List<BoardResponseDto>>> boardList = boardCrudService.boardList();

        return new ResponseEntity<>(getBoardResponseDtos(boardList), boardList.getStatusCode());
    }

    /**
     * 검색 옵션을 통해서 게시물 조회
     * @param searchOption 검색 옵션
     * @param content 검색할 문자열
     * @return 게시물 리스트
     */
    @GetMapping("/board/{search-option}/{content}")
    public ResponseEntity<List<BoardResponseDto>> searchOptionBoard(@PathVariable("search-option") SearchOption searchOption,
                                                                    @PathVariable("content") String content) {
        return boardCrudService.search(searchOption, content);
    }

    /**
     * 게시물 삭제
     *
     * @param boardRequestDto
     * @return 게시물 삭제 완료, OK
     */
    @DeleteMapping("/board")
    public ResponseEntity<String> boardDelete(@RequestBody @Valid BoardRequestDto boardRequestDto) {

        ResponseEntity<Optional<BoardResponseDto>> boardDelete =
                boardCrudService.boardDelete(boardRequestDto.getId(), boardRequestDto.getMemberRequestDto().getUsername());

        return new ResponseEntity<>(BOARD_DELETE_SUCCESS, boardDelete.getStatusCode());
    }

    /**
     * 게시물 제목 중복 검사
     *
     * @param boardRequestDto
     * @return 중복을 찾는데에 성공하면 True 와 OK, 실패하면 False 와 NOT_FOUND
     */
    @PostMapping("/board/duplicated/title")
    public ResponseEntity<String> boardDuplicatedTitleCheck(@RequestBody @Valid BoardRequestDto boardRequestDto) {

        Boolean titleExistValidation = boardCrudService.boardTitleExistValidation(boardRequestDto);

        if (titleExistValidation) {
            return new ResponseEntity<>(titleExistValidation.toString(), OK);
        } else {
            return new ResponseEntity<>(titleExistValidation.toString(), NOT_FOUND);
        }
    }

    /**
     * 게시물 목록 Optional Wrapping 해제 후 반환
     *
     * @param boardList
     * @return 게시물 목록
     */
    private List<BoardResponseDto> getBoardResponseDtos(ResponseEntity<Optional<List<BoardResponseDto>>> boardList) {
        return boardList.getBody().get();
    }

    /**
     * 게시물 Dto Optional Wrapping 해제 후 반환
     *
     * @param board
     * @return 게시물 응답 Dto
     */
    private BoardResponseDto getBoard(ResponseEntity<Optional<BoardResponseDto>> board) {
        return getBody(board).get();
    }

    /**
     * ResponseEntity Wrapping 해제 후 반환
     *
     * @param boardCreate
     * @return Optionally Wrapped 댓글 응답 Dto
     */
    private Optional<BoardResponseDto> getBody(ResponseEntity<Optional<BoardResponseDto>> boardCreate) {
        return boardCreate.getBody();
    }
}


