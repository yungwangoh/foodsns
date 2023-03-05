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
import sejong.foodsns.repository.file.BoardFileRepository;
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
    @Transactional
    public List<BoardFile> saveBoardFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<BoardFile> boardFiles = boardFileStorage.storeFiles(multipartFiles);


        return boardFiles;
    }
}
