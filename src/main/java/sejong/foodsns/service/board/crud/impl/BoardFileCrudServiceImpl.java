package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.file.util.BoardFileStorage;
import sejong.foodsns.dto.board.BoardFileRequestDto;
import sejong.foodsns.dto.board.BoardFileResponseDto;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.repository.file.BoardFileRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardFileCrudService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardFileCrudServiceImpl implements BoardFileCrudService {

    private final BoardFileRepository boardFileRepository;
    private final BoardFileStorage boardFileStorage;

    @Override
    public List<BoardFile> saveBoardFiles(Map<BoardFileType, List<MultipartFile>> multipartFileListMap) throws IOException {
        List<BoardFile> imageFiles = boardFileStorage.storeFiles(multipartFileListMap.get(BoardFileType.IMAGE), BoardFileType.IMAGE);
        List<BoardFile> generalFiles = boardFileStorage.storeFiles(multipartFileListMap.get(BoardFileType.GENERAL), BoardFileType.GENERAL);

        return Stream.of(imageFiles, generalFiles)
                .flatMap(Collection::stream).toList();
    }

    @Override
    public Map<BoardFileType, List<BoardFile>> findBoardFiles() {
        List<BoardFile> boardFiles = boardFileRepository.findAll();
        Map<BoardFileType, List<BoardFile>> result = boardFiles.stream()
                .collect(Collectors.groupingBy(BoardFile::getBoardFileType));

        return result;
    }

    @Override
    public ResponseEntity<Optional<BoardFileResponseDto>> boardFileGet(BoardFileRequestDto boardFileRequestDto) {
        BoardFile boardFile = boardFileRepository.findById(boardFileRequestDto.getId()).get();

        BoardFile build = BoardFile.builder()
                .originFileName(boardFile.getOriginFilename())
                .uuid(boardFile.getUuid())
                .board(boardFile.getBoard())
                .build();

        return new ResponseEntity<>(of(new BoardFileResponseDto(build)), CREATED);
    }

}
