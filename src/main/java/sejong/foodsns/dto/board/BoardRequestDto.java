package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardRequestDto {

    private Long id;
    private String title;
    private String content;
    private MemberRequestDto memberRequestDto;
    private Long check;
    private int recommCount;
    private Map<BoardFileType, List<MultipartFile>> boardFiles;

    @Builder
    public BoardRequestDto(String title, String content, Map<BoardFileType, List<MultipartFile>> boardFiles) {
        this.title = title;
        this.content = content;
        this.boardFiles = boardFiles;
    }

    public Board toEntity() {

        return Board.builder()
                .title(title)
                .content(content)
                .memberRank(memberRequestDto.toEntity().getMemberRank())
                .check(0L)
                .recommCount(0)
                .member(memberRequestDto.toEntity())
                .boardFiles(new ArrayList<>())
                .build();
    }
}
