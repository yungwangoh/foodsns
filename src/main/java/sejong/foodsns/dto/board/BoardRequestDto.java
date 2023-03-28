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
public class BoardRequestDto {

    @Schema(description = "게시물 제목", example = "김치찌개 레시피")
    private String title;
    @Schema(description = "게시물 내용", example = "김치찌개 레시피 메뉴얼")
    private String content;
    @Schema(description = "게시물 작성자", example = "하윤")
    private String username;

    @Builder
    public BoardRequestDto(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }
}
