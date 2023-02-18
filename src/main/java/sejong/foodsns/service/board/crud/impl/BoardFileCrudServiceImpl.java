package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.dto.board.BoardFileRequestDto;
import sejong.foodsns.dto.board.BoardFileResponseDto;
import sejong.foodsns.repository.file.BoardFileRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.board.crud.BoardFileCrudService;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardFileCrudServiceImpl implements BoardFileCrudService {

    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;


    @Override
    public ResponseEntity<Optional<BoardFileResponseDto>> boardFileSave(BoardFileRequestDto boardFileRequestDto) {

        String filePath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + boardFileRequestDto.getFileName();

        BoardFile saveBoardFile = boardFileRepository.save(boardFileRequestDto.toEntity());
        return new ResponseEntity<>(of(new BoardFileResponseDto(saveBoardFile)), CREATED);
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
