package sejong.foodsns.service.board.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.util.BoardFileStorage;
import sejong.foodsns.service.board.crud.BoardFileCrudService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardFileCrudServiceImpl implements BoardFileCrudService {
    private final BoardFileStorage boardFileStorage;

    @Override
    @Transactional
    public List<BoardFile> saveBoardFiles(List<MultipartFile> multipartFiles, Board board) throws IOException {

        return boardFileStorage.storeFiles(multipartFiles, board);
    }
}
