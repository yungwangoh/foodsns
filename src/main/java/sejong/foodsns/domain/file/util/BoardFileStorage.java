package sejong.foodsns.domain.file.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BoardFileStorage {

    @Value("${file.dir}/")
    private String boardFileDirPath;

    // 파일 확장자명 추출
    private String extractExt(String filename) {
        int index = filename.lastIndexOf(".");
        String ext = filename.substring(index);

        return ext;
    }

    private String createStoreFilename(String filename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(filename);
        String saveFilename = uuid + ext;

        return saveFilename;
    }

    public String createPath(String storeFilename, BoardFileType boardFileType) {
        if (boardFileType == BoardFileType.IMAGE) {
            return boardFileDirPath + "images/" + storeFilename;
        }
        return boardFileDirPath + "generals/" + storeFilename;
    }

    public BoardFile storeFile(MultipartFile multipartFile, BoardFileType boardFileType) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String saveFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(createPath(saveFilename, boardFileType)));

        return BoardFile.builder()
                .originFileName(originalFilename)
                .storePath(saveFilename)
                .boardFileType(boardFileType)
                .build();

    }

    public List<BoardFile> storeFiles(List<MultipartFile> multipartFiles, BoardFileType boardFileType) throws IOException {
        List<BoardFile> boardFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                boardFiles.add(storeFile(multipartFile, boardFileType));
            }
        }
        return boardFiles;
    }
}
