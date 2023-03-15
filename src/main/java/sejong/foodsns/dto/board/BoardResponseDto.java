package sejong.foodsns.dto.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.file.BoardFile;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private MemberRank memberRank;
    private Long check;
    private int recommCount;
    private MemberResponseDto memberResponseDto;
    private List<BoardFile> boardFiles;


    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberResponseDto = new MemberResponseDto(board.getMember());
        this.memberRank = board.getMemberRank();
        this.check = board.getCheck();
        this.recommCount = board.getRecommCount();
        this.boardFiles = board.getBoardFiles();
    }
}
