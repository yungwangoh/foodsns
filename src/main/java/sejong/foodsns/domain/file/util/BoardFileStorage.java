package sejong.foodsns.domain.file.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.repository.file.BoardFileRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardFileStorage {

    @Value("/Users/yungwang-o/Documents/board_file/")
    private String boardFileDirPath;
    private final BoardFileRepository boardFileRepository;
    private static final String PNG = ".png";
    private static final String JPEG = ".jpeg";


    // 파일 확장자명 추출
    private String extractExt(String filename) {
        int index = filename.lastIndexOf(".");

        return filename.substring(index);
    }

    private String createStoreFilename(String filename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(filename);

        if(!onlyImageExtAllows(ext))
            throw new IllegalArgumentException("지원하는 이미지 파일 포맷 형식이 아닙니다.");

        return uuid + ext;
    }

    private static boolean onlyImageExtAllows(String ext) {

        if(ext.equals(PNG) || ext.equals(JPEG)) {
            return true;
        } else {
            return false;
        }
    }

    public String createPath(String storeFilename) {
        return boardFileDirPath + storeFilename;
    }

    public BoardFile storeFile(MultipartFile multipartFile, Board board) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String saveFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(createPath(saveFilename)));

        return BoardFile.builder()
                .originFileName(originalFilename)
                .storePath(saveFilename)
                .board(board)
                .build();

    }

    public List<BoardFile> storeFiles(List<MultipartFile> multipartFiles, Board board) throws IOException {
        List<BoardFile> boardFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                BoardFile boardFile = boardFileRepository.save(storeFile(multipartFile, board));
                boardFiles.add(boardFile);
            }
        }

        return boardFiles;
    }
}
