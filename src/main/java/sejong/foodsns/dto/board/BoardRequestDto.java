package sejong.foodsns.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;
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

import static sejong.foodsns.domain.member.MemberType.NORMAL;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardRequestDto {

    @Schema(description = "게시물 id")
    private Long id;
    @Schema(description = "게시물 제목", example = "김치찌개 레시피")
    private String title;
    @Schema(description = "게시물 내용", example = "김치찌개 레시피 메뉴얼")
    private String content;
    @Schema(description = "게시물 작성자", example = "하윤")
    private MemberRequestDto memberRequestDto;
    @Schema(description = "게시물 첨부파일", example = "완성된 김치찌개.jpg")
    private Map<BoardFileType, List<MultipartFile>> boardFiles;

    @Builder
    public BoardRequestDto(Long id, String title, String content, Member member, @Nullable Map<BoardFileType, List<MultipartFile>> boardFiles) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberRequestDto = new MemberRequestDto(member.getUsername(), member.getEmail(), member.getPassword());
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
                .build();
    }
}
