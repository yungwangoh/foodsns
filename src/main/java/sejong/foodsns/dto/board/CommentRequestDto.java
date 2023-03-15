package sejong.foodsns.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.domain.file.BoardFileType;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentRequestDto {

    private Long id;
    @Schema(description = "댓글 내용", example = "레시피 후기 Good.")
    private String content;
    @Schema(description = "댓글 작성자", example = "Mr.광")
    private MemberRequestDto memberRequestDto;
    @Schema(description = "댓글 달린 게시물", example = "김치찌개 레시피 게시물")
    private BoardRequestDto boardRequestDto;
}
