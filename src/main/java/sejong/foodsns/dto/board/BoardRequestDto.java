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

    @Builder
    public BoardRequestDto(Long id, String title, String content, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberRequestDto = new MemberRequestDto(member.getUsername(), member.getEmail(), member.getPassword());
    }
}
